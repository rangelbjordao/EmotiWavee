package org.example.emotiwave.application.dto.out;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public record RegistroHumorResponseDto(
        Long id,
        String humor,
        List<String> atividades,
        String detalhes,
        OffsetDateTime criadoEm
) {}