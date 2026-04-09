package org.example.emotiwave.application.service.UsuarioService;

import org.example.emotiwave.application.dto.in.RegistroHumorRequestDto;
import org.example.emotiwave.application.dto.out.RegistroHumorResponseDto;
import org.example.emotiwave.domain.entities.RegistroHumor;
import org.example.emotiwave.domain.entities.Usuario;
import org.example.emotiwave.infra.client.ApexClient;
import org.example.emotiwave.infra.repository.RegistroHumorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
}