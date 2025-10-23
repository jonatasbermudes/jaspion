package com.alertsfromtv.service;

import com.alertsfromtv.dto.ConexaoRequestDto;
import com.alertsfromtv.entity.BotTelegram;
import com.alertsfromtv.entity.ConexaoWebhook;
import com.alertsfromtv.dto.ConexaoRequestDto;
import com.alertsfromtv.entity.BotTelegram;
import com.alertsfromtv.entity.ConexaoWebhook;
import com.alertsfromtv.entity.DestinoTelegram;
import com.alertsfromtv.entity.Usuario;
import com.alertsfromtv.exception.ResourceNotFoundException;
import com.alertsfromtv.repository.BotTelegramRepository;
import com.alertsfromtv.repository.ConexaoWebhookRepository;
import com.alertsfromtv.repository.DestinoTelegramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ConexaoService {

    @Autowired
    private ConexaoWebhookRepository conexaoRepository;

    @Autowired
    private BotTelegramRepository botRepository;

    @Autowired
    private DestinoTelegramRepository destinoRepository;

    public ConexaoWebhook createConexao(ConexaoRequestDto conexaoDto, Usuario usuario) {
        BotTelegram bot = botRepository.findById(conexaoDto.getBotId())
            .orElseThrow(() -> new ResourceNotFoundException("Bot not found with id " + conexaoDto.getBotId()));
        DestinoTelegram destino = destinoRepository.findById(conexaoDto.getDestinoId())
            .orElseThrow(() -> new ResourceNotFoundException("Destino not found with id " + conexaoDto.getDestinoId()));

        ConexaoWebhook conexao = new ConexaoWebhook();
        conexao.setNome(conexaoDto.getNome());
        conexao.setAtivo(conexaoDto.isAtivo());
        conexao.setTemplateMensagem(conexaoDto.getTemplateMensagem());
        conexao.setUsuario(usuario);
        conexao.setBot(bot);
        conexao.setDestino(destino);
        return conexaoRepository.save(conexao);
    }

    public List<ConexaoWebhook> getConexoesByUsuario(Usuario usuario) {
        return conexaoRepository.findByUsuarioId(usuario.getId());
    }

    public ConexaoWebhook updateConexao(UUID id, ConexaoRequestDto conexaoDto, Usuario usuario) {
        ConexaoWebhook conexao = conexaoRepository.findById(id)
                .filter(c -> c.getUsuario().getId().equals(usuario.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Conexao not found with id " + id));

        BotTelegram bot = botRepository.findById(conexaoDto.getBotId())
                .orElseThrow(() -> new ResourceNotFoundException("Bot not found with id " + conexaoDto.getBotId()));
        DestinoTelegram destino = destinoRepository.findById(conexaoDto.getDestinoId())
                .orElseThrow(() -> new ResourceNotFoundException("Destino not found with id " + conexaoDto.getDestinoId()));

        conexao.setNome(conexaoDto.getNome());
        conexao.setAtivo(conexaoDto.isAtivo());
        conexao.setTemplateMensagem(conexaoDto.getTemplateMensagem());
        conexao.setBot(bot);
        conexao.setDestino(destino);
        return conexaoRepository.save(conexao);
    }

    public void deleteConexao(UUID id, Usuario usuario) {
        ConexaoWebhook conexao = conexaoRepository.findById(id)
                .filter(c -> c.getUsuario().getId().equals(usuario.getId()))
                .orElseThrow(() -> new RuntimeException("Conexao not found"));
        conexaoRepository.delete(conexao);
    }
}
