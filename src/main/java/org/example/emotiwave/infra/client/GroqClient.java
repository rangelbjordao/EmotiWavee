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

    public String gerarRecomendacao(String humorMedio) {

        String prompt = """
                Você é a IA do aplicativo EmotiWave.
                
                O humor médio do usuário hoje é:
                %s
                
                Gere uma recomendação curta em português brasileiro natural.
                
                Regras obrigatórias:
                - apenas 1 frase
                - máximo 14 palavras
                - linguagem simples e natural
                - tom leve e acolhedor
                - foco em bem-estar digital
                - sugira hábitos genéricos e simples
                - não cite horários do dia
                - não invente funcionalidades do app
                - não mencione meditação, terapia ou técnicas especiais
                - evite construções formais
                - não use emojis
                - não use aspas
                - não use exclamações
                - não descreva cenários específicos
                """.formatted(humorMedio);

        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("model", "llama-3.1-8b-instant");
        requestBody.put("temperature", 0.8);
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