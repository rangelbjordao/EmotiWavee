package org.example.emotiwave.application.service.UsuarioService;

import org.example.emotiwave.application.dto.out.AnaliseEmocionalIAResponseDto;
import org.example.emotiwave.domain.entities.RegistroHumor;
import org.example.emotiwave.domain.entities.Usuario;
import org.example.emotiwave.infra.repository.RegistroHumorRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnaliseEmocionalIAService {

    private final RegistroHumorRepository repository;

    public AnaliseEmocionalIAService(RegistroHumorRepository repository) {
        this.repository = repository;
    }

    public AnaliseEmocionalIAResponseDto analisar(Usuario usuario) {

        List<RegistroHumor> registros =
                repository.findByUsuarioIdOrderByCriadoEmDesc(usuario.getId());

        if (registros.isEmpty()) {
            return new AnaliseEmocionalIAResponseDto(
                    0.0,
                    "sem dados",
                    "não identificado",
                    "nenhuma",
                    "Registre seu humor para gerar análises."
            );
        }

        List<RegistroHumor> ordenados = registros.stream()
                .sorted(Comparator.comparing(RegistroHumor::getCriadoEm))
                .toList();

        List<Integer> scores = ordenados.stream()
                .map(r -> converter(r.getHumor()))
                .toList();

        double media = scores.stream()
                .mapToInt(i -> i)
                .average()
                .orElse(0.0);

        String tendencia = calcularTendencia(scores);
        String atividade = atividadeNegativaMaisFrequente(ordenados);
        String estado = classificarEstado(media);
        String recomendacao = gerarRecomendacao(estado, tendencia, atividade);

        return new AnaliseEmocionalIAResponseDto(
                arredondar(media),
                tendencia,
                estado,
                atividade,
                recomendacao
        );
    }

    private int converter(String humor) {
        if (humor == null) return 3;

        return switch (humor.toLowerCase()) {
            case "empolgado" -> 5;
            case "feliz" -> 4;
            case "neutro" -> 3;
            case "infeliz" -> 2;
            case "triste" -> 1;
            default -> 3;
        };
    }

    private String calcularTendencia(List<Integer> scores) {
        if (scores.size() < 2) return "estável";

        int meio = scores.size() / 2;

        double inicio = scores.subList(0, meio).stream().mapToInt(i -> i).average().orElse(0);
        double fim = scores.subList(meio, scores.size()).stream().mapToInt(i -> i).average().orElse(0);

        double diff = fim - inicio;

        if (diff >= 0.5) return "melhorando";
        if (diff <= -0.5) return "piorando";
        return "estável";
    }

    private String atividadeNegativaMaisFrequente(List<RegistroHumor> registros) {
        Map<String, Integer> contagem = new HashMap<>();

        for (RegistroHumor r : registros) {
            int score = converter(r.getHumor());

            if (score <= 2 && r.getAtividades() != null) {
                for (String a : r.getAtividades()) {
                    contagem.merge(a, 1, Integer::sum);
                }
            }
        }

        return contagem.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("nenhuma identificada");
    }

    private String classificarEstado(double media) {
        if (media <= 2.0) return "alerta emocional";
        if (media <= 3.4) return "atenção";
        return "estável";
    }

    private String gerarRecomendacao(String estado, String tendencia, String atividade) {

        if ("alerta emocional".equals(estado)) {
            return "Identificamos sinais de alerta emocional. Atividade associada: "
                    + atividade + ". Considere reduzir essa carga e cuidar do seu bem-estar.";
        }

        if ("atenção".equals(estado)) {
            return "Seu estado emocional requer atenção. Continue registrando seu diário.";
        }

        if ("melhorando".equals(tendencia)) {
            return "Seu humor está melhorando. Continue com hábitos positivos.";
        }

        return "Seu estado emocional está estável.";
    }

    private double arredondar(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}