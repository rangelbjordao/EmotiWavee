package org.example.emotiwave.infra.client;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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
        String path = "/registros";

        try {
            System.out.println("=== CHAMANDO APEX: ENVIAR REGISTRO ===");
            System.out.println("URL: " + APEX_BASE_URL + path);
            System.out.println("Payload usuario_id: " + usuarioId);
            System.out.println("Payload humor: " + humor);

            String resposta = webClient.post()
                    .uri(path)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "usuario_id", usuarioId,
                            "humor", humor,
                            "detalhes", detalhes != null ? detalhes : ""
                    ))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("Sucesso ao enviar para APEX.");
            System.out.println("Resposta: " + resposta);

        } catch (WebClientResponseException e) {
            System.out.println("Erro HTTP ao enviar para APEX");
            System.out.println("Status: " + e.getStatusCode());
            System.out.println("Body: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println("Erro geral ao enviar para APEX: " + e.getMessage());
            e.printStackTrace();
        }
    }
}