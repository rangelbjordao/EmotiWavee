package org.example.emotiwave.web.controller;

import org.example.emotiwave.application.dto.out.AnaliseEmocionalIAResponseDto;
import org.example.emotiwave.application.service.UsuarioService.AnaliseEmocionalIAService;
import org.example.emotiwave.domain.entities.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ia")
public class AnaliseEmocionalIAController {

    private final AnaliseEmocionalIAService service;

    public AnaliseEmocionalIAController(AnaliseEmocionalIAService service) {
        this.service = service;
    }

    @GetMapping("/analise-emocional")
    public ResponseEntity<AnaliseEmocionalIAResponseDto> analisar(
            @AuthenticationPrincipal Usuario usuario
    ) {
        return ResponseEntity.ok(service.analisar(usuario));
    }
}