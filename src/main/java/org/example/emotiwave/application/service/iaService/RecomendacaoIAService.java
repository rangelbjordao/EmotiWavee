package org.example.emotiwave.application.service.iaService;

import org.example.emotiwave.application.dto.out.RecomendacaoIAResponse;
import org.example.emotiwave.domain.entities.RegistroHumor;
import org.example.emotiwave.domain.entities.Usuario;
import org.example.emotiwave.infra.client.GroqClient;
import org.example.emotiwave.infra.repository.RegistroHumorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecomendacaoIAService {

    private final GroqClient groqClient;
    private final RegistroHumorRepository registroHumorRepository;

    private int valorHumor(String humor) {
        return switch (humor) {
            case "Empolgado" -> 90;
            case "Feliz" -> 75;
            case "Neutro" -> 55;
            case "Infeliz" -> 35;
            case "Triste" -> 15;
            default -> 55;
        };
    }

    private String obterHumorPelaMedia(int media) {
        if (media >= 80) return "Empolgado";
        if (media >= 65) return "Feliz";
        if (media >= 45) return "Neutro";
        if (media >= 25) return "Infeliz";
        return "Triste";
    }

    public RecomendacaoIAService(GroqClient groqClient,
                                 RegistroHumorRepository registroHumorRepository) {
        this.groqClient = groqClient;
        this.registroHumorRepository = registroHumorRepository;
    }

    public RecomendacaoIAResponse gerarParaUsuario(Usuario usuario) {

        LocalDateTime inicioDoDia = LocalDate.now()
                .atStartOfDay();

        List<String> humores = registroHumorRepository
                .findByUsuarioIdOrderByCriadoEmDesc(usuario.getId())
                .stream()
                .filter(r -> r.getCriadoEm() != null
                        && r.getCriadoEm().isAfter(inicioDoDia))
                .map(RegistroHumor::getHumor)
                .toList();

        if (humores.isEmpty()) {
            return new RecomendacaoIAResponse(
                    "Registre seu humor hoje para receber uma recomendação personalizada.",
                    "Neutro"
            );
        }

        int media = (int) humores.stream()
                .mapToInt(this::valorHumor)
                .average()
                .orElse(55);

        String humorMedio = obterHumorPelaMedia(media);

        String recomendacao =
                groqClient.gerarRecomendacao(humorMedio);

        return new RecomendacaoIAResponse(
                recomendacao,
                humorMedio
        );
    }
}