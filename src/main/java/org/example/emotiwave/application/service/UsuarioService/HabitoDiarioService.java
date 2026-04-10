package org.example.emotiwave.application.service.UsuarioService;

import org.example.emotiwave.application.dto.in.HabitoDiarioRequestDto;
import org.example.emotiwave.application.dto.out.HabitoDiarioResponseDto;
import org.example.emotiwave.domain.entities.HabitoDiario;
import org.example.emotiwave.domain.entities.Usuario;
import org.example.emotiwave.infra.repository.HabitoDiarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitoDiarioService {

    private final HabitoDiarioRepository repository;

    public HabitoDiarioService(HabitoDiarioRepository repository) {
        this.repository = repository;
    }

    public HabitoDiarioResponseDto criar(HabitoDiarioRequestDto dto, Usuario usuario) {
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
                .stream().map(this::toDto).toList();
    }

    public HabitoDiarioResponseDto atualizar(Long id, HabitoDiarioRequestDto dto, Usuario usuario) {
        HabitoDiario habito = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hábito não encontrado"));

        if (!habito.getUsuario().getId().equals(usuario.getId()))
            throw new RuntimeException("Sem permissão");

        habito.setAtividade(dto.getAtividade());
        habito.setValor(dto.getValor());
        habito.setUnidade(dto.getUnidade());
        habito.setDataRegistro(dto.getDataRegistro());

        return toDto(repository.save(habito));
    }

    public void deletar(Long id, Usuario usuario) {
        HabitoDiario habito = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hábito não encontrado"));

        if (!habito.getUsuario().getId().equals(usuario.getId()))
            throw new RuntimeException("Sem permissão");

        repository.delete(habito);
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