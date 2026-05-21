package org.example.emotiwave.application.service.UsuarioService;

import org.example.emotiwave.application.dto.in.RegistroHumorRequestDto;
import org.example.emotiwave.application.dto.out.RegistroHumorResponseDto;
import org.example.emotiwave.domain.entities.RegistroHumor;
import org.example.emotiwave.domain.entities.Usuario;
import org.example.emotiwave.infra.client.ApexClient;
import org.example.emotiwave.infra.repository.RegistroHumorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RegistroHumorService {

    private final RegistroHumorRepository repository;
    private final ApexClient apexClient;

    public RegistroHumorService(RegistroHumorRepository repository, ApexClient apexClient) {
        this.repository = repository;
        this.apexClient = apexClient;
    }

    public RegistroHumorResponseDto criar(RegistroHumorRequestDto dto, Usuario usuario) {
        RegistroHumor registro = new RegistroHumor();
        registro.setUsuario(usuario);
        registro.setHumor(dto.humor());
        registro.setAtividades(dto.atividades());
        registro.setDetalhes(dto.detalhes());
        RegistroHumor salvo = repository.save(registro);
        // Envia para o APEX
        apexClient.enviarRegistro(usuario.getId(), dto.humor(), dto.detalhes());
        return toDto(salvo);
    }

    public List<RegistroHumorResponseDto> listar(Usuario usuario) {
        return repository.findByUsuarioIdOrderByCriadoEmDesc(usuario.getId())
                .stream().map(this::toDto).toList();
    }

    public RegistroHumorResponseDto atualizar(Long id, RegistroHumorRequestDto dto, Usuario usuario) {
        RegistroHumor registro = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro não encontrado"));
        if (!registro.getUsuario().getId().equals(usuario.getId()))
            throw new RuntimeException("Sem permissão");
        registro.setHumor(dto.humor());
        registro.setAtividades(dto.atividades());
        registro.setDetalhes(dto.detalhes());
        return toDto(repository.save(registro));
    }

    public void deletar(Long id, Usuario usuario) {
        RegistroHumor registro = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro não encontrado"));
        if (!registro.getUsuario().getId().equals(usuario.getId()))
            throw new RuntimeException("Sem permissão");
        repository.delete(registro);
    }

    private RegistroHumorResponseDto toDto(RegistroHumor r) {
        return new RegistroHumorResponseDto(
                r.getId(), r.getHumor(), r.getAtividades(),
                r.getDetalhes(), r.getCriadoEm());
    }

    public Map<String, Object> gerarRelatorioSemanal(Usuario usuario) {
        var limite = java.time.LocalDateTime.now().minusDays(7);

        var registros = repository.findByUsuarioIdOrderByCriadoEmDesc(usuario.getId())
                .stream()
                .filter(r -> r.getCriadoEm() != null && r.getCriadoEm().isAfter(limite))
                .toList();

        if (registros.isEmpty()) {
            return Map.of(
                    "humor_predominante", "Sem registros",
                    "total_registros", 0,
                    "media_humor", 0,
                    "ultimo_registro", ""
            );
        }

        var contagem = registros.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        RegistroHumor::getHumor,
                        java.util.stream.Collectors.counting()
                ));

        String humorPredominante = contagem.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Sem registros");

        double media = registros.stream()
                .mapToInt(r -> converterHumorParaNota(r.getHumor()))
                .average()
                .orElse(0);

        var ultimoRegistro = registros.stream()
                .map(RegistroHumor::getCriadoEm)
                .filter(java.util.Objects::nonNull)
                .max(java.time.LocalDateTime::compareTo)
                .orElse(null);

        return Map.of(
                "humor_predominante", humorPredominante,
                "total_registros", registros.size(),
                "media_humor", Math.round(media * 10.0) / 10.0,
                "ultimo_registro", ultimoRegistro != null ? ultimoRegistro.toString() : ""
        );
    }

    private int converterHumorParaNota(String humor) {
        return switch (humor) {
            case "Empolgado" -> 5;
            case "Feliz" -> 4;
            case "Neutro" -> 3;
            case "Infeliz" -> 2;
            case "Triste" -> 1;
            default -> 3;
        };
    }
}