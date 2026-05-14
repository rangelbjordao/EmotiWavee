package org.example.emotiwave.web.controller;

import org.example.emotiwave.application.service.iaService.RecomendacaoIAService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ia")
@CrossOrigin(origins = "*")
public class IAController {

    private final RecomendacaoIAService recomendacaoIAService;

    public IAController(RecomendacaoIAService recomendacaoIAService) {
        this.recomendacaoIAService = recomendacaoIAService;
    }

    @GetMapping("/recomendacao")
    public ResponseEntity<?> recomendacao(
            @AuthenticationPrincipal Object usuario) {

        System.out.println("USUARIO:");

        return ResponseEntity.ok("teste funcionando");
    }
}