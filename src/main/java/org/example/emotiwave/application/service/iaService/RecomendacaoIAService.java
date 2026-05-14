package org.example.emotiwave.application.service.iaService;

import org.example.emotiwave.domain.entities.RegistroHumor;
import org.example.emotiwave.domain.entities.Usuario;
import org.example.emotiwave.infra.client.GroqClient;
import org.example.emotiwave.infra.repository.RegistroHumorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecomendacaoIAService {

    private final GroqClient groqClient;
    private final RegistroHumorRepository registroHumorRepository;

    public RecomendacaoIAService(GroqClient groqClient,
                                 RegistroHumorRepository registroHumorRepository) {
        this.groqClient = groqClient;
        this.registroHumorRepository = registroHumorRepository;
    }

    public String gerarParaUsuario(Usuario usuario) {
        LocalDateTime inicioDoDia = LocalDateTime.now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        List<String> humores = registroHumorRepository
                .findByUsuarioIdOrderByCriadoEmDesc(usuario.getId())
                .stream()
                .filter(r -> r.getCriadoEm() != null
                        && r.getCriadoEm().isAfter(inicioDoDia))
                .map(RegistroHumor::getHumor)
                .toList();

        if (humores.isEmpty()) {
            return "Você ainda não registrou seus humores essa semana. " +
                    "Comece hoje para receber recomendações personalizadas!";
        }

        return groqClient.gerarRecomendacao(humores);
    }
}