package org.example.emotiwave.application.dto.out;

import java.util.List;

public record EstatisticaResponse(
        String artista_predominante,
        String genero_predominante,
        int total_musicas
) {}
