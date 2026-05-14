package org.example.emotiwave.application.dto.out;

public class RecomendacaoIAResponse {

    private String recomendacao;
    private String humor;

    public RecomendacaoIAResponse(String recomendacao, String humor) {
        this.recomendacao = recomendacao;
        this.humor = humor;
    }

    public String getRecomendacao() {
        return recomendacao;
    }

    public String getHumor() {
        return humor;
    }
}