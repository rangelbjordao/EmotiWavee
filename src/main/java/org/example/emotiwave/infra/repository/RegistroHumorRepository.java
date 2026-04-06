package org.example.emotiwave.infra.repository;

import org.example.emotiwave.domain.entities.RegistroHumor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RegistroHumorRepository extends JpaRepository<RegistroHumor, Long> {
    List<RegistroHumor> findByUsuarioIdOrderByCriadoEmDesc(Long usuarioId);
}