package org.example.emotiwave.application.service.spotifyServices;

import jakarta.transaction.Transactional;
import org.example.emotiwave.application.dto.in.MusicaSimplesDto;
import org.example.emotiwave.application.dto.in.MusicasUsuarioSpotifyDto;
import org.example.emotiwave.application.mapper.MusicaMapper;
import org.example.emotiwave.application.service.GeniusLyricsService;
import org.example.emotiwave.domain.entities.FonteMusica;
import org.example.emotiwave.domain.entities.Musica;
import org.example.emotiwave.domain.entities.Usuario;
import org.example.emotiwave.domain.entities.UsuarioMusica;
import org.example.emotiwave.infra.client.SpotifyClient;
import org.example.emotiwave.infra.repository.MusicaRepository;
import org.example.emotiwave.infra.repository.UsuarioMusicaRepository;
import org.example.emotiwave.infra.repository.UsuarioRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopMusicasSpotifyService {
    private final MusicaRepository musicaRepository;
    private final MusicaMapper musicaMapper;
    private final SpotifyService spotifyService;
    private final GeniusLyricsService geniusLyricsService;
    private final UsuarioMusicaRepository usuarioMusicaRepository;

    public TopMusicasSpotifyService(
            MusicaRepository musicaRepository,
            MusicaMapper musicaMapper,
            SpotifyService spotifyService,
            GeniusLyricsService geniusLyricsService,
            UsuarioRepository usuarioRepository,
            SpotifyClient spotifyClient,
            UsuarioMusicaRepository usuarioMusicaRepository
    ) {
        this.musicaRepository = musicaRepository;
        this.musicaMapper = musicaMapper;
        this.spotifyService = spotifyService;
        this.geniusLyricsService = geniusLyricsService;
        this.usuarioMusicaRepository = usuarioMusicaRepository;
    }

    public List<MusicaSimplesDto> buscarTopMusicasSpotify(Usuario usuario, int limit) {
        this.spotifyService.verificarExpiracaoToken(usuario);

        int limitFinal = Math.max(1, Math.min(limit, 10));

        String spotifyTopTracksUrl =
                "https://api.spotify.com/v1/me/top/tracks?time_range=short_term&limit=" + limitFinal;

        MusicasUsuarioSpotifyDto dtoSpotify = this.spotifyService.enviarRequisicaoSpotifyUtilsV2(
                usuario,
                spotifyTopTracksUrl,
                new ParameterizedTypeReference<MusicasUsuarioSpotifyDto>() {
                },
                null
        );

        return this.converterTopMusicasParaDto(dtoSpotify, usuario);
    }

    private List<MusicaSimplesDto> converterTopMusicasParaDto(MusicasUsuarioSpotifyDto dtoSpotify, Usuario usuario) {
        List<MusicaSimplesDto> topMusicas = new ArrayList<>();

        if (dtoSpotify != null && dtoSpotify.getItems() != null) {
            for (MusicasUsuarioSpotifyDto.Track track : dtoSpotify.getItems()) {
                if (track.getArtists() != null && !track.getArtists().isEmpty()) {
                    String artistaId = track.getArtists().get(0).getId();
                    String generoString = "Desconhecido";
                    String imagemUrl = track.getImagemUrl();

                    topMusicas.add(
                            new MusicaSimplesDto(
                                    track.getName(),
                                    track.getArtistsNames(),
                                    track.getId(),
                                    artistaId,
                                    generoString,
                                    imagemUrl
                            )
                    );
                }
            }
        }

        return topMusicas;
    }

    @Transactional
    protected void converterTopMusicasParaEntidade(List<MusicaSimplesDto> topMusicas, Usuario usuario) {
        for (MusicaSimplesDto topMusicaDto : topMusicas) {
            if (this.musicaRepository.findBySpotifyTrackId(topMusicaDto.getSpotifyTrackId()) == null) {
                Musica musicaEntity = this.musicaMapper.toEntity(topMusicaDto);
                String letra = this.geniusLyricsService.buscarLyrics(
                        topMusicaDto.getArtista(),
                        topMusicaDto.getTitulo()
                );
                musicaEntity.setLetra(letra);
                musicaEntity.setSpotifyTrackId(topMusicaDto.getSpotifyTrackId());
                musicaEntity.setGenero(String.valueOf(topMusicaDto.getGenero()));
                musicaRepository.save(musicaEntity);

                UsuarioMusica usuarioMusica = new UsuarioMusica();
                usuarioMusica.setMusica(musicaEntity);
                usuarioMusica.setUsuario(usuario);
                usuarioMusica.setFonte(FonteMusica.SPOTIFY);
                this.usuarioMusicaRepository.save(usuarioMusica);
            }
        }
    }
}