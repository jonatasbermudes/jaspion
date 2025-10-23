package com.alertsfromtv.controller;

import com.alertsfromtv.dto.BotDto;
import com.alertsfromtv.entity.BotTelegram;
import com.alertsfromtv.entity.Usuario;
import com.alertsfromtv.repository.UsuarioRepository;
import com.alertsfromtv.service.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bots")
public class BotController {

    @Autowired
    private BotService botService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<BotDto> createBot(@RequestBody BotDto botDto, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = getUsuarioFromUserDetails(userDetails);
        BotTelegram bot = botService.createBot(botDto, usuario);
        return ResponseEntity.ok(convertToDto(bot));
    }

    @GetMapping
    public ResponseEntity<List<BotDto>> getBots(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = getUsuarioFromUserDetails(userDetails);
        List<BotTelegram> bots = botService.getBotsByUsuario(usuario);
        return ResponseEntity.ok(bots.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BotDto> getBot(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = getUsuarioFromUserDetails(userDetails);
        BotTelegram bot = botService.getBotByIdAndUsuario(id, usuario);
        return ResponseEntity.ok(convertToDto(bot));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BotDto> updateBot(@PathVariable Long id, @RequestBody BotDto botDto, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = getUsuarioFromUserDetails(userDetails);
        BotTelegram bot = botService.updateBot(id, botDto, usuario);
        return ResponseEntity.ok(convertToDto(bot));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBot(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = getUsuarioFromUserDetails(userDetails);
        botService.deleteBot(id, usuario);
        return ResponseEntity.noContent().build();
    }

    private Usuario getUsuarioFromUserDetails(UserDetails userDetails) {
        return usuarioRepository.findByEmail(userDetails.getUsername()).orElseThrow();
    }

    private BotDto convertToDto(BotTelegram bot) {
        BotDto dto = new BotDto();
        dto.setId(bot.getId());
        dto.setNome(bot.getNome());
        dto.setTokenApi(bot.getTokenApi());
        return dto;
    }
}
