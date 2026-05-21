package org.example.emotiwave.application.dto.in;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Setter
@Getter
public class HabitoDiarioRequestDto {

    private String atividade;
    private BigDecimal valor;
    private String unidade;
    private LocalDate dataRegistro;
    private OffsetDateTime criadoEm;
}