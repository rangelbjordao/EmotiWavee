package org.example.emotiwave.web.controller;

import org.example.emotiwave.application.dto.in.RegistroHumorRequestDto;
import org.example.emotiwave.application.dto.out.RegistroHumorResponseDto;
import org.example.emotiwave.application.service.UsuarioService.RegistroHumorService;
import org.example.emotiwave.domain.entities.Usuario;
import org.example.emotiwave.infra.client.ApexClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/humor")
public class RegistroHumorController {

    private final RegistroHumorService service;

    public RegistroHumorController(RegistroHumorService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RegistroHumorResponseDto> criar(
            @RequestBody RegistroHumorRequestDto dto,
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.status(201).body(service.criar(dto, usuario));
    }

    @GetMapping
    public ResponseEntity<List<RegistroHumorResponseDto>> listar(
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(service.listar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegistroHumorResponseDto> atualizar(
            @PathVariable Long id,
            @RequestBody RegistroHumorRequestDto dto,
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(service.atualizar(id, dto, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {
        service.deletar(id, usuario);
        return ResponseEntity.noContent().build();
    }

    @Autowired
    private ApexClient apexClient;

    @GetMapping("/relatorio-semanal")
    public ResponseEntity<Map<String, Object>> relatorioSemanal(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(service.gerarRelatorioSemanal(usuario));
    }
}