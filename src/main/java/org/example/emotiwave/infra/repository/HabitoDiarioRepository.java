package org.example.emotiwave.infra.repository;

import org.example.emotiwave.domain.entities.HabitoDiario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitoDiarioRepository extends JpaRepository<HabitoDiario, Long> {

    List<HabitoDiario> findByUsuarioIdOrderByCriadoEmDesc(Long usuarioId);
}