package com.alertsfromtv.controller;

import com.alertsfromtv.dto.DestinoDto;
import com.alertsfromtv.entity.DestinoTelegram;
import com.alertsfromtv.entity.Usuario;
import com.alertsfromtv.repository.UsuarioRepository;
import com.alertsfromtv.service.DestinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/destinos")
public class DestinoController {

    @Autowired
    private DestinoService destinoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<DestinoDto> createDestino(@RequestBody DestinoDto destinoDto, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = getUsuarioFromUserDetails(userDetails);
        DestinoTelegram destino = destinoService.createDestino(destinoDto, usuario);
        return ResponseEntity.ok(convertToDto(destino));
    }

    @GetMapping
    public ResponseEntity<List<DestinoDto>> getDestinos(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = getUsuarioFromUserDetails(userDetails);
        List<DestinoTelegram> destinos = destinoService.getDestinosByUsuario(usuario);
        return ResponseEntity.ok(destinos.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinoDto> getDestino(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = getUsuarioFromUserDetails(userDetails);
        DestinoTelegram destino = destinoService.getDestinoByIdAndUsuario(id, usuario);
        return ResponseEntity.ok(convertToDto(destino));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DestinoDto> updateDestino(@PathVariable Long id, @RequestBody DestinoDto destinoDto, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = getUsuarioFromUserDetails(userDetails);
        DestinoTelegram destino = destinoService.updateDestino(id, destinoDto, usuario);
        return ResponseEntity.ok(convertToDto(destino));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDestino(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = getUsuarioFromUserDetails(userDetails);
        destinoService.deleteDestino(id, usuario);
        return ResponseEntity.noContent().build();
    }

    private Usuario getUsuarioFromUserDetails(UserDetails userDetails) {
        return usuarioRepository.findByEmail(userDetails.getUsername()).orElseThrow();
    }

    private DestinoDto convertToDto(DestinoTelegram destino) {
        DestinoDto dto = new DestinoDto();
        dto.setId(destino.getId());
        dto.setNome(destino.getNome());
        dto.setChatId(destino.getChatId());
        return dto;
    }
}
