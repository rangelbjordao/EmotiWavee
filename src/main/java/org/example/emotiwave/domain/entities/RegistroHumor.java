package org.example.emotiwave.domain.entities;

import jakarta.persistence.*;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "T_REGISTRO_HUMOR")
public class RegistroHumor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Setter
    private String humor;

    @Setter
    @ElementCollection
    @CollectionTable(name = "T_REGISTRO_ATIVIDADES", joinColumns = @JoinColumn(name = "registro_id"))
    @Column(name = "atividade")
    private List<String> atividades;

    @Setter
    private String detalhes;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }

    public String getHumor() { return humor; }

    public List<String> getAtividades() { return atividades; }

    public String getDetalhes() { return detalhes; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
}