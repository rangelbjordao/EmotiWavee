package org.example.emotiwave.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "T_HABITO_DIARIO")
public class HabitoDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Setter
    private String atividade;

    @Setter
    private BigDecimal valor;

    @Setter
    private String unidade;

    @Setter
    @Column(name = "data_registro")
    private LocalDate dataRegistro;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }

}