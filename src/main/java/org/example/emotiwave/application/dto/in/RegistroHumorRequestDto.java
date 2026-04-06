package org.example.emotiwave.application.dto.in;

import java.util.List;

public record RegistroHumorRequestDto(
        String humor,
        List<String> atividades,
        String detalhes
) {}