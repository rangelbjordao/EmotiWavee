package org.example.emotiwave.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
public class TesteApexController {

    @GetMapping("/teste-apex")
    public ResponseEntity<String> testarApex() {
        String url = "https://oracleapex.com/ords/wksp_emotiwave/humor/registros";

        try {
            RestTemplate restTemplate = new RestTemplate();
            String resposta = restTemplate.getForObject(url, String.class);

            System.out.println("Resposta do APEX: " + resposta);
            return ResponseEntity.ok("Sucesso: " + resposta);

        } catch (HttpClientErrorException e) {
            System.out.println("Erro HTTP ao chamar APEX");
            System.out.println("Status: " + e.getStatusCode());
            System.out.println("Body: " + e.getResponseBodyAsString());

            return ResponseEntity
                    .status(e.getStatusCode())
                    .body("Erro HTTP: " + e.getStatusCode() + " | Body: " + e.getResponseBodyAsString());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Erro geral: " + e.getMessage());
        }
    }
}
