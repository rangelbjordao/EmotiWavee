package org.example.emotiwave.application.dto.in;

public class MusicaSimplesDto {
    private String titulo;
    private String artista;
    private String spotifyTrackId;
    private String artistaId;
    private String genero;
    private String imagemUrl;

    public MusicaSimplesDto() {
    }

    public MusicaSimplesDto(
            String titulo,
            String artista,
            String spotifyTrackId,
            String artistaId,
            String genero,
            String imagemUrl
    ) {
        this.titulo = titulo;
        this.artista = artista;
        this.spotifyTrackId = spotifyTrackId;
        this.artistaId = artistaId;
        this.genero = genero;
        this.imagemUrl = imagemUrl;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getArtista() {
        return artista;
    }

    public String getSpotifyTrackId() {
        return spotifyTrackId;
    }

    public String getArtistaId() {
        return artistaId;
    }

    public String getGenero() {
        return genero;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public void setSpotifyTrackId(String spotifyTrackId) {
        this.spotifyTrackId = spotifyTrackId;
    }

    public void setArtistaId(String artistaId) {
        this.artistaId = artistaId;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }
}