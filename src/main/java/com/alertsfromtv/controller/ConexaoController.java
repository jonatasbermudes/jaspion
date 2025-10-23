package com.alertsfromtv.controller;

import com.alertsfromtv.dto.ConexaoRequestDto;
import com.alertsfromtv.dto.ConexaoResponseDto;
import com.alertsfromtv.entity.ConexaoWebhook;
import com.alertsfromtv.entity.Usuario;
import com.alertsfromtv.repository.UsuarioRepository;
import com.alertsfromtv.service.ConexaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/conexoes")
public class ConexaoController {

    @Autowired
    private ConexaoService conexaoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<ConexaoResponseDto> createConexao(@RequestBody ConexaoRequestDto conexaoDto, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = getUsuarioFromUserDetails(userDetails);
        ConexaoWebhook conexao = conexaoService.createConexao(conexaoDto, usuario);
        return ResponseEntity.ok(convertToDto(conexao));
    }

    @GetMapping
    public ResponseEntity<List<ConexaoResponseDto>> getConexoes(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = getUsuarioFromUserDetails(userDetails);
        List<ConexaoWebhook> conexoes = conexaoService.getConexoesByUsuario(usuario);
        return ResponseEntity.ok(conexoes.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConexaoResponseDto> updateConexao(@PathVariable UUID id, @RequestBody ConexaoRequestDto conexaoDto, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = getUsuarioFromUserDetails(userDetails);
        ConexaoWebhook conexao = conexaoService.updateConexao(id, conexaoDto, usuario);
        return ResponseEntity.ok(convertToDto(conexao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConexao(@PathVariable UUID id, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = getUsuarioFromUserDetails(userDetails);
        conexaoService.deleteConexao(id, usuario);
        return ResponseEntity.noContent().build();
    }

    private Usuario getUsuarioFromUserDetails(UserDetails userDetails) {
        return usuarioRepository.findByEmail(userDetails.getUsername()).orElseThrow();
    }

    private ConexaoResponseDto convertToDto(ConexaoWebhook conexao) {
        ConexaoResponseDto dto = new ConexaoResponseDto();
        dto.setId(conexao.getId());
        dto.setNome(conexao.getNome());
        dto.setAtivo(conexao.isAtivo());
        dto.setTemplateMensagem(conexao.getTemplateMensagem());
        dto.setBotId(conexao.getBot().getId());
        dto.setDestinoId(conexao.getDestino().getId());
        return dto;
    }
}
