package org.example.emotiwave.web.controller;

import org.example.emotiwave.application.dto.in.HabitoDiarioRequestDto;
import org.example.emotiwave.application.dto.out.HabitoDiarioResponseDto;
import org.example.emotiwave.application.service.UsuarioService.HabitoDiarioService;
import org.example.emotiwave.domain.entities.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habitos")
public class HabitoDiarioController {

    private final HabitoDiarioService service;

    public HabitoDiarioController(HabitoDiarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<HabitoDiarioResponseDto> criar(
            @RequestBody HabitoDiarioRequestDto dto,
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.status(201).body(service.criar(dto, usuario));
    }

    @GetMapping
    public ResponseEntity<List<HabitoDiarioResponseDto>> listar(
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(service.listar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitoDiarioResponseDto> atualizar(
            @PathVariable Long id,
            @RequestBody HabitoDiarioRequestDto dto,
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
}