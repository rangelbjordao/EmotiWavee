package org.example.emotiwave.infra.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import org.example.emotiwave.application.dto.out.AccessTokenResponseDto;
import org.example.emotiwave.domain.entities.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Component
public class SpotifyClient {
    private final ObjectMapper objectMapper;
    private final String scopes = "user-top-read user-read-private user-read-recently-played";
    private final WebClient webClient;
    @Value("${spotify.redirect-uri}")
    private String redirectUri;
    @Value("${spotify.client-id}")
    String clientId;
    @Value("${spotify.client-secret}")
    String secret;

    public SpotifyClient(ObjectMapper objectMapper, WebClient webClient) {
        this.objectMapper = objectMapper;
        this.webClient = webClient;
    }

    public String construirAutorizacao(String authHeader) {
        String jwt = authHeader.replace("Bearer ", "");

        return UriComponentsBuilder
                .fromHttpUrl("https://accounts.spotify.com/authorize")
                .queryParam("client_id", this.clientId)
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope", scopes)
                .queryParam("state", jwt)
                .queryParam("show_dialog", "true") // 👈 ESSENCIAL
                .build()
                .toUriString();
    }

    public AccessTokenResponseDto exchangeCodeForTokens(String code) {
        try {
            String basicAuth = Base64.getEncoder().encodeToString((this.clientId + ":" + this.secret).getBytes(StandardCharsets.UTF_8));

            MultiValueMap<String, String> formData = new LinkedMultiValueMap();
            formData.add("grant_type", "authorization_code");
            formData.add("code", code);
            formData.add("redirect_uri", redirectUri);

            WebClient webClient = WebClient.builder().baseUrl("https://accounts.spotify.com").build();

            return webClient.post().uri("/api/token").header("Authorization", "Basic " + basicAuth).contentType(MediaType.APPLICATION_FORM_URLENCODED).bodyValue(formData).retrieve().bodyToMono(AccessTokenResponseDto.class).block();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao trocar código por tokens do Spotify", e);
        }
    }

    public AccessTokenResponseDto refreshAccessToken(Usuario usuario) {
        try {
            String refreshToken = usuario.getSpotifyInfo().getRefreshToken();
            String basicAuth = Base64.getEncoder().encodeToString((this.clientId + ":" + this.secret).getBytes(StandardCharsets.UTF_8));
            MultiValueMap<String, String> formData = new LinkedMultiValueMap();
            formData.add("grant_type", "refresh_token");
            formData.add("refresh_token", URLEncoder.encode(refreshToken, StandardCharsets.UTF_8));
            return this.webClient.post().uri("https://accounts.spotify.com/api/token", new Object[0]).header("Authorization", new String[]{"Basic " + basicAuth}).contentType(MediaType.APPLICATION_FORM_URLENCODED).bodyValue(formData).retrieve().bodyToMono(AccessTokenResponseDto.class).block();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao renovar access token do Spotify", e);
        }
    }

    public <T> T enviarRequisicaoSpotifyUtils(Usuario usuario, String url, ParameterizedTypeReference<T> responseType, @Nullable Long after) {
        try {
            String urlFinal = UriComponentsBuilder.fromHttpUrl(url).queryParamIfPresent("after", Optional.ofNullable(after)).toUriString();
            return this.webClient.get().uri(urlFinal, new Object[0]).header("Authorization", new String[]{"Bearer " + usuario.getSpotifyInfo().getAccessToken()}).retrieve().bodyToMono(responseType).block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Falha ao consultar Spotify: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao chamar a API do Spotify", e);
        }
    }
}
