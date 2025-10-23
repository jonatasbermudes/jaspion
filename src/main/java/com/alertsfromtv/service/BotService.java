package com.alertsfromtv.service;

import com.alertsfromtv.dto.BotDto;
import com.alertsfromtv.dto.BotDto;
import com.alertsfromtv.entity.BotTelegram;
import com.alertsfromtv.entity.Usuario;
import com.alertsfromtv.exception.ResourceNotFoundException;
import com.alertsfromtv.repository.BotTelegramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BotService {

    @Autowired
    private BotTelegramRepository botRepository;

    public BotTelegram createBot(BotDto botDto, Usuario usuario) {
        BotTelegram bot = new BotTelegram();
        bot.setNome(botDto.getNome());
        bot.setTokenApi(botDto.getTokenApi());
        bot.setUsuario(usuario);
        return botRepository.save(bot);
    }

    public List<BotTelegram> getBotsByUsuario(Usuario usuario) {
        return botRepository.findByUsuarioId(usuario.getId());
    }

    public BotTelegram getBotByIdAndUsuario(Long id, Usuario usuario) {
        return botRepository.findById(id)
                .filter(bot -> bot.getUsuario().getId().equals(usuario.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Bot not found with id " + id));
    }

    public BotTelegram updateBot(Long id, BotDto botDto, Usuario usuario) {
        BotTelegram bot = getBotByIdAndUsuario(id, usuario);
        bot.setNome(botDto.getNome());
        bot.setTokenApi(botDto.getTokenApi());
        return botRepository.save(bot);
    }

    public void deleteBot(Long id, Usuario usuario) {
        BotTelegram bot = getBotByIdAndUsuario(id, usuario);
        botRepository.delete(bot);
    }
}
