package org.example.emotiwave.application.service;


import jakarta.validation.Valid;
import org.example.emotiwave.application.dto.in.DadosAuthRequestDto;
import org.example.emotiwave.application.dto.in.UsuarioCreateRequestDto;
import org.example.emotiwave.application.dto.out.DadosTokenJwtResponseDto;
import org.example.emotiwave.domain.entities.Usuario;
import org.example.emotiwave.domain.exceptions.AutenticacaoFalhou;
import org.example.emotiwave.infra.repository.UsuarioRepository;
import org.example.emotiwave.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void criarUsuario(UsuarioCreateRequestDto dados) {
        if (usuarioRepository.findByEmail(dados.email()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }
        Usuario usuario = new Usuario();
        usuario.setUsername(dados.username());
        usuario.setEmail(dados.email());
        usuario.setPassword(passwordEncoder.encode(dados.password()));
        usuarioRepository.save(usuario);
    }

    public AutenticacaoService() {
    }

    public DadosTokenJwtResponseDto autenticar(@Valid DadosAuthRequestDto dados) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(dados.email(), dados.password());

            UserDetails userDetails =
                    (UserDetails) this.authenticationManager.authenticate(authenticationToken).getPrincipal();

            String tokenJwt = this.tokenService.gerarToken(userDetails);

            Usuario usuario = usuarioRepository.findByEmail(dados.email())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            return new DadosTokenJwtResponseDto(tokenJwt, usuario.getNomeUsuario());
        } catch (AuthenticationException e) {
            throw new AutenticacaoFalhou("Usuário ou senha inválidos");
        }
    }
}
