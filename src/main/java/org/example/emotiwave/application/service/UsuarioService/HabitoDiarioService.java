package org.example.emotiwave.application.service.UsuarioService;

import org.example.emotiwave.application.dto.in.HabitoDiarioRequestDto;
import org.example.emotiwave.application.dto.out.HabitoDiarioResponseDto;
import org.example.emotiwave.domain.entities.HabitoDiario;
import org.example.emotiwave.domain.entities.Usuario;
import org.example.emotiwave.infra.repository.HabitoDiarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class HabitoDiarioService {

    private final HabitoDiarioRepository repository;

    public HabitoDiarioService(HabitoDiarioRepository repository) {
        this.repository = repository;
    }

    public HabitoDiarioResponseDto criar(HabitoDiarioRequestDto dto, Usuario usuario) {
        validarHabito(dto);

        HabitoDiario habito = new HabitoDiario();
        habito.setUsuario(usuario);
        habito.setAtividade(dto.getAtividade());
        habito.setValor(dto.getValor());
        habito.setUnidade(dto.getUnidade());
        habito.setDataRegistro(dto.getDataRegistro());

        return toDto(repository.save(habito));
    }

    public List<HabitoDiarioResponseDto> listar(Usuario usuario) {
        return repository.findByUsuarioIdOrderByCriadoEmDesc(usuario.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    public HabitoDiarioResponseDto atualizar(Long id, HabitoDiarioRequestDto dto, Usuario usuario) {
        validarHabito(dto);

        HabitoDiario habito = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hábito não encontrado"));

        if (!habito.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Sem permissão");
        }

        habito.setAtividade(dto.getAtividade());
        habito.setValor(dto.getValor());
        habito.setUnidade(dto.getUnidade());
        habito.setDataRegistro(dto.getDataRegistro());

        return toDto(repository.save(habito));
    }

    public void deletar(Long id, Usuario usuario) {
        HabitoDiario habito = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hábito não encontrado"));

        if (!habito.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Sem permissão");
        }

        repository.delete(habito);
    }

    private void validarHabito(HabitoDiarioRequestDto dto) {
        if (dto == null) {
            throw new RuntimeException("Dados do hábito não informados.");
        }

        if (dto.getAtividade() == null || dto.getAtividade().isBlank()) {
            throw new RuntimeException("Atividade é obrigatória.");
        }

        if (dto.getUnidade() == null || dto.getUnidade().isBlank()) {
            throw new RuntimeException("Unidade é obrigatória.");
        }

        if (dto.getValor() == null) {
            throw new RuntimeException("Valor é obrigatório.");
        }

        if (dto.getDataRegistro() == null) {
            throw new RuntimeException("Data do registro é obrigatória.");
        }

        String atividade = dto.getAtividade().trim();
        String unidade = dto.getUnidade().trim();
        BigDecimal valor = dto.getValor();

        switch (atividade) {
            case "Sono" -> validarFaixa(valor, 1, 12, "Sono");
            case "Água" -> validarFaixa(valor, 1, 20, "Água");
            case "Exercício" -> validarFaixa(valor, 1, 300, "Exercício");
            case "Estudo" -> validarFaixa(valor, 1, 12, "Estudo");
            default -> throw new RuntimeException("Atividade inválida.");
        }

        switch (atividade) {
            case "Água" -> {
                if (!unidade.equals("copos")) {
                    throw new RuntimeException("Unidade inválida para Água.");
                }
            }
            case "Exercício" -> {
                if (!unidade.equals("minutos")) {
                    throw new RuntimeException("Unidade inválida para Exercício.");
                }
            }
        }
    }

    private void validarFaixa(BigDecimal valor, int minimo, int maximo, String atividade) {
        if (valor.compareTo(BigDecimal.valueOf(minimo)) < 0 ||
                valor.compareTo(BigDecimal.valueOf(maximo)) > 0) {
            throw new RuntimeException(
                    atividade + " deve estar entre " + minimo + " e " + maximo + "."
            );
        }
    }

    private HabitoDiarioResponseDto toDto(HabitoDiario h) {
        return new HabitoDiarioResponseDto(
                h.getId(),
                h.getAtividade(),
                h.getValor(),
                h.getUnidade(),
                h.getDataRegistro(),
                h.getCriadoEm()
        );
    }
}