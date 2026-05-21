package org.example.emotiwave.application.dto.out;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record HabitoDiarioResponseDto(Long id, String atividade, BigDecimal valor, String unidade,
                                      LocalDate dataRegistro, OffsetDateTime criadoEm ) {

}