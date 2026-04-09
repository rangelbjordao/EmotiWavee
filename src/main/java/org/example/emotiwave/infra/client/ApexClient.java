package org.example.emotiwave.infra.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import java.util.Map;

@Component
public class ApexClient {

    private final WebClient webClient;
    private static final String APEX_BASE_URL = "https://oracleapex.com/ords/wksp_emotiwave/humor";

    public ApexClient() {
        this.webClient = WebClient.builder()
                .baseUrl(APEX_BASE_URL)
                .build();
    }

    public void enviarRegistro(Long usuarioId, String humor, String detalhes) {
        try {
            webClient.post()
                    .uri("/registros")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "usuario_id", usuarioId,
                            "humor", humor,
                            "detalhes", detalhes != null ? detalhes : ""
                    ))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            System.out.println("Erro ao enviar para APEX: " + e.getMessage());
        }
    }

    public Map buscarRelatorio(Long usuarioId) {
        try {
            return webClient.get()
                    .uri("/relatorio?usuario_id=" + usuarioId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (Exception e) {
            System.out.println("Erro ao buscar relatório APEX: " + e.getMessage());
            return Map.of();
        }
    }
}