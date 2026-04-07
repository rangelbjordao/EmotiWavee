package org.example.emotiwave.application.dto.out;

public record AnaliseEmocionalIAResponseDto(
        double humorMedio,
        String tendencia,
        String estado,
        String atividadeRelacionada,
        String recomendacao
) {}