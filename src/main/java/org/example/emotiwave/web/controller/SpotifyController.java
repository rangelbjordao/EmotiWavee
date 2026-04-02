package org.example.emotiwave.web.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.emotiwave.application.service.spotifyServices.SpotifyService;
import org.example.emotiwave.domain.entities.Usuario;
import org.example.emotiwave.infra.security.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/spotify"})
@Tag(
        name = "SpotifyAuth",
        description = "Gerenciamento da autenticaçãp com o  spotify"
)
public class SpotifyController {
    private final SpotifyService spotifyService;
    private final TokenService tokenService;

    public SpotifyController(SpotifyService spotifyService, TokenService tokenService) {
        this.spotifyService = spotifyService;
        this.tokenService = tokenService;
    }

    @Operation(
            summary = "Redirecionar usuário para login do Spotify",
            description = "Gera a URL de autorização do Spotify para o usuário se conectar à sua conta. É necessário passar o token JWT no header Authorization."
    )
    @ApiResponse(
            responseCode = "200",
            description = "URL de autorização gerada com sucesso"
    )
    @GetMapping({"/auth"})
    public ResponseEntity<String> redirecionarParaLoginSpotify(@RequestHeader("Authorization") String authHeader) {
        String authUrl = this.spotifyService.solicitarAutorizacao(authHeader);
        return ResponseEntity.ok(authUrl);
    }

    @Operation(
            summary = "Callback do Spotify após login",
            description = "Recebe o código de autorização do Spotify e o token JWT do usuário (state) para trocar pelo access token. Atualiza os tokens do usuário no sistema."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Código de autorização recebido e processado com sucesso"
    )
    @GetMapping({"/callback"})
    public ResponseEntity<?> spotifyCallback(@RequestParam("code") String code, @RequestParam("state") String jwt) {
        Usuario usuario = this.tokenService.getUsuarioFromToken(jwt);
        System.out.println(code);
        this.spotifyService.exchangeCodeForTokens(code, usuario);
        return ResponseEntity.ok("""
                <!DOCTYPE html>
                <html>
                <head>
                  <title>Conectado</title>
                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                  <style>
                    body {
                      font-family: Arial, sans-serif;
                      text-align: center;
                      margin-top: 60px;
                      padding: 20px;
                    }
                
                    h2 {
                      color: #1DB954;
                    }
                
                    button {
                      margin-top: 20px;
                      padding: 12px 20px;
                      font-size: 16px;
                      border: none;
                      border-radius: 8px;
                      background-color: #1DB954;
                      color: white;
                      cursor: pointer;
                    }
                
                    button:active {
                      opacity: 0.8;
                    }
                  </style>
                </head>
                <body>
                  <h2>Spotify conectado!</h2>
                  <p>Você já pode voltar para o aplicativo.</p>
                
                  <button onclick="window.close()">Fechar</button>
                
                  <script>
                    // tenta fechar automaticamente
                    setTimeout(() => {
                      window.close();
                    }, 2000);
                  </script>
                </body>
                </html>
                """);
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> verificarConexaoSpotify(@AuthenticationPrincipal Usuario usuario) {
        boolean conectado = usuario.getSpotifyInfo() != null
                && usuario.getSpotifyInfo().getAccessToken() != null;
        return ResponseEntity.ok(conectado);
    }

    @DeleteMapping("/disconnect")
    public ResponseEntity<Void> desconectarSpotify(@AuthenticationPrincipal Usuario usuario) {
        spotifyService.desconectar(usuario);
        return ResponseEntity.ok().build();
    }
}
