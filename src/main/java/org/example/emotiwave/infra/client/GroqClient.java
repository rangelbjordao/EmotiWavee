package org.example.emotiwave.infra.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class GroqClient {

    private final WebClient webClient;

    @Value("${groq.api.key}")
    private String apiKey;

    public GroqClient() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.groq.com/openai/v1")
                .build();
    }

    public String gerarRecomendacao(List<String> humores) {

        String historico = String.join(", ", humores);

        String prompt = """
                Você é uma IA de bem-estar digital integrada ao EmotiWave, \
                um app que monitora o impacto do consumo digital na saúde emocional.
                
                Histórico emocional do usuário nos últimos dias:
                %s
                
                Com base nesse histórico, gere uma recomendação curta (máximo 3 frases), \
                acolhedora e positiva em português. \
                Foque em hábitos digitais saudáveis e bem-estar emocional. \
                Não use listas, apenas texto corrido.
                """.formatted(historico);

        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("model", "llama-3.1-8b-instant");
        requestBody.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
        ));

        @SuppressWarnings("unchecked")
        Map<String, Object> groqResponse = webClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .doOnNext(errorBody ->
                                        System.err.println("Groq erro 4xx: " + errorBody))
                                .then(Mono.error(new RuntimeException("Erro na API do Groq")))
                )
                .bodyToMono(Map.class)
                .block();

        try {
            if (groqResponse == null) {
                return "Não foi possível gerar recomendação no momento. Cuide-se!";
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> choices =
                    (List<Map<String, Object>>) groqResponse.get("choices");

            @SuppressWarnings("unchecked")
            Map<String, Object> message =
                    (Map<String, Object>) choices.getFirst().get("message");

            return message.get("content").toString();

        } catch (Exception e) {
            return "Não foi possível gerar recomendação no momento. Cuide-se!";
        }
    }
}