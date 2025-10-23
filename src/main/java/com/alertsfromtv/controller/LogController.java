package com.alertsfromtv.controller;

import com.alertsfromtv.dto.LogDto;
import com.alertsfromtv.entity.LogExecucao;
import com.alertsfromtv.entity.Usuario;
import com.alertsfromtv.repository.UsuarioRepository;
import com.alertsfromtv.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    @Autowired
    private LogService logService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/conexao/{conexaoId}")
    public ResponseEntity<List<LogDto>> getLogsByConexao(@PathVariable UUID conexaoId, @AuthenticationPrincipal UserDetails userDetails, Pageable pageable) {
        Usuario usuario = getUsuarioFromUserDetails(userDetails);
        List<LogExecucao> logs = logService.getLogsByConexaoAndUsuario(conexaoId, usuario, pageable);
        return ResponseEntity.ok(logs.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    private Usuario getUsuarioFromUserDetails(UserDetails userDetails) {
        return usuarioRepository.findByEmail(userDetails.getUsername()).orElseThrow();
    }

    private LogDto convertToDto(LogExecucao log) {
        LogDto dto = new LogDto();
        dto.setId(log.getId());
        dto.setTimestamp(log.getTimestamp());
        dto.setSucesso(log.isSucesso());
        dto.setBodyRecebido(log.getBodyRecebido());
        dto.setRespostaTelegram(log.getRespostaTelegram());
        return dto;
    }
}
