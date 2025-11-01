package com.alertsfromtv.controller;

import com.alertsfromtv.dto.BotDto;
import com.alertsfromtv.dto.ConexaoRequestDto;
import com.alertsfromtv.dto.ConexaoResponseDto;
import com.alertsfromtv.dto.DestinoDto;
import com.alertsfromtv.entity.ConexaoWebhook;
import com.alertsfromtv.entity.Usuario;
import com.alertsfromtv.service.ConexaoService;
import com.alertsfromtv.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<ConexaoResponseDto> createConexao(@RequestBody ConexaoRequestDto conexaoDto, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioService.getUsuarioByEmail(email);
        ConexaoWebhook conexao = conexaoService.createConexao(conexaoDto, usuario);
        return ResponseEntity.ok(convertToDto(conexao));
    }

    @GetMapping
    public ResponseEntity<List<ConexaoResponseDto>> getConexoes(Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioService.getUsuarioByEmail(email);
        List<ConexaoWebhook> conexoes = conexaoService.getConexoesByUsuario(usuario);
        List<ConexaoResponseDto> dtos = conexoes.stream().map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConexaoResponseDto> getConexao(@PathVariable UUID id, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioService.getUsuarioByEmail(email);
        ConexaoWebhook conexao = conexaoService.getConexaoByIdAndUsuario(id, usuario);
        return ResponseEntity.ok(convertToDto(conexao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConexaoResponseDto> updateConexao(@PathVariable UUID id, @RequestBody ConexaoRequestDto conexaoDto, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioService.getUsuarioByEmail(email);
        ConexaoWebhook conexao = conexaoService.updateConexao(id, conexaoDto, usuario);
        return ResponseEntity.ok(convertToDto(conexao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConexao(@PathVariable UUID id, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioService.getUsuarioByEmail(email);
        conexaoService.deleteConexao(id, usuario);
        return ResponseEntity.noContent().build();
    }

    private ConexaoResponseDto convertToDto(ConexaoWebhook conexao) {
        ConexaoResponseDto dto = new ConexaoResponseDto();
        dto.setId(conexao.getId());
        dto.setNome(conexao.getNome());
        dto.setAtivo(conexao.isAtivo());
        dto.setTemplateMensagem(conexao.getTemplateMensagem());

        BotDto botDto = new BotDto();
        botDto.setId(conexao.getBot().getId());
        botDto.setNome(conexao.getBot().getNome());
        dto.setBot(botDto);

        DestinoDto destinoDto = new DestinoDto();
        destinoDto.setId(conexao.getDestino().getId());
        destinoDto.setNome(conexao.getDestino().getNome());
        destinoDto.setChatId(conexao.getDestino().getChatId());
        dto.setDestino(destinoDto);

        return dto;
    }
}
