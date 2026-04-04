package org.example.emotiwave.application.service.spotifyServices;

import org.example.emotiwave.application.dto.in.MusicaSimplesDto;
import org.example.emotiwave.application.dto.out.EstatisticaResponse;
import org.example.emotiwave.domain.entities.Usuario;
import org.example.emotiwave.infra.repository.AnaliseMusicaRepository;
import org.example.emotiwave.infra.repository.UsuarioMusicaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EstatisticaService {

    private final UsuarioMusicaRepository usuarioMusicaRepository;
    private final AnaliseMusicaRepository analiseMusicaRepository;
    private final TopMusicasSpotifyService topMusicasSpotifyService;

    public EstatisticaService(UsuarioMusicaRepository usuarioMusicaRepository, AnaliseMusicaRepository analiseMusicaRepository, TopMusicasSpotifyService topMusicasSpotifyService) {
        this.usuarioMusicaRepository = usuarioMusicaRepository;
        this.analiseMusicaRepository = analiseMusicaRepository;
        this.topMusicasSpotifyService = topMusicasSpotifyService;
    }

    public EstatisticaResponse gerarEstatistica(Usuario usuario, LocalDate inicio, LocalDate fim) {

        List<MusicaSimplesDto> musicas =
                topMusicasSpotifyService.buscarTopMusicasSpotify(usuario, 10);

        if (musicas == null || musicas.isEmpty()) {
            return new EstatisticaResponse(null, null, 0);
        }

        Map<String, Integer> contagemArtista = new HashMap<>();
        Map<String, Integer> contagemGenero = new HashMap<>();

        for (MusicaSimplesDto m : musicas) {
            contagemArtista.merge(m.getArtista(), 1, Integer::sum);
            contagemGenero.merge(
                    m.getGenero() != null ? m.getGenero() : "Desconhecido",
                    1,
                    Integer::sum
            );
        }

        String artistaPredominante = contagemArtista.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();

        String generoPredominante = contagemGenero.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();

        return new EstatisticaResponse(
                artistaPredominante,
                generoPredominante,
                musicas.size()
        );
    }
}
