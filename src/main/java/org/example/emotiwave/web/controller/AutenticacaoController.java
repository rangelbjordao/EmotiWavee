package org.example.emotiwave.web.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.emotiwave.application.dto.in.DadosAuthRequestDto;
import org.example.emotiwave.application.dto.in.UsuarioCreateRequestDto;
import org.example.emotiwave.application.dto.out.DadosTokenJwtResponseDto;
import org.example.emotiwave.application.service.AutenticacaoService;
import org.example.emotiwave.domain.exceptions.AutenticacaoFalhou;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping({"/auth"})
@Tag(
        name = "Autenticação",
        description = "Gerenciamento de autenticações"
)

public class AutenticacaoController {
    @Autowired
    private AutenticacaoService autenticacaoService;

    public AutenticacaoController() {
    }

    @Operation(
            summary = "Autenticação do usuario no Sistema",
            description = "Endpoint responsavel por verificar autenticidade do usuario no sistema",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Autenticação bem-sucedida e token JWT retornado",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = DadosTokenJwtResponseDto.class
                            )
                    )}
            ), @ApiResponse(
                    responseCode = "401",
                    description = "Credenciais inválidass"
            ), @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida"
            )}
    )
    @ApiResponses
    @PostMapping
    public ResponseEntity efetuarAutenticacao(@RequestBody @Valid DadosAuthRequestDto dados) {
        DadosTokenJwtResponseDto resposta = this.autenticacaoService.autenticar(dados);
        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/criar")
    public ResponseEntity criarUsuario(@RequestBody UsuarioCreateRequestDto dados) {
        autenticacaoService.criarUsuario(dados);
        return ResponseEntity.status(201).build();
    }

    @ExceptionHandler(AutenticacaoFalhou.class)
    public ResponseEntity<Map<String, String>> handleAutenticacaoFalhou(AutenticacaoFalhou ex) {
        return ResponseEntity.status(401).body(Map.of("mensagem", ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(400).body(Map.of("mensagem", ex.getMessage()));
    }
}
