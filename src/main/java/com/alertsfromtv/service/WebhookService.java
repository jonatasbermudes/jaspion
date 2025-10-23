package com.alertsfromtv.service;

import com.alertsfromtv.entity.ConexaoWebhook;
import com.alertsfromtv.entity.LogExecucao;
import com.alertsfromtv.repository.ConexaoWebhookRepository;
import com.alertsfromtv.repository.LogExecucaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.Instant;
import java.util.UUID;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.json.JSONObject;

@Service
public class WebhookService {

    @Autowired
    private ConexaoWebhookRepository conexaoRepository;

    @Autowired
    private LogExecucaoRepository logExecucaoRepository;

    @Async
    public void processarWebhook(UUID conexaoId, String body) {
        ConexaoWebhook conexao = conexaoRepository.findById(conexaoId).orElse(null);

        if (conexao == null || !conexao.isAtivo()) {
            return;
        }

        LogExecucao log = new LogExecucao();
        log.setConexao(conexao);
        log.setTimestamp(Instant.now());
        log.setBodyRecebido(body);

        try {
            String token = conexao.getBot().getTokenApi();
            String chatId = conexao.getDestino().getChatId();

            String mensagem = conexao.getTemplateMensagem();
            if (mensagem == null || mensagem.trim().isEmpty()) {
                mensagem = body; // Fallback to raw body
            } else {
                try {
                    JSONObject jsonBody = new JSONObject(body);
                    for (String key : jsonBody.keySet()) {
                        String placeholder = "{{" + key + "}}";
                        String value = jsonBody.get(key).toString();
                        mensagem = mensagem.replace(placeholder, value);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error processing message template with given body", e);
                }
            }

            DefaultBotOptions botOptions = new DefaultBotOptions();
            DefaultAbsSender bot = new DefaultAbsSender(botOptions, token) {
                @Override
                public String getBotToken() {
                    return token;
                }
            };

            SendMessage message = new SendMessage(chatId, mensagem);
            bot.execute(message);

            log.setSucesso(true);
            log.setRespostaTelegram("OK");
        } catch (TelegramApiException e) {
            log.setSucesso(false);
            log.setRespostaTelegram(e.getMessage());
        } finally {
            logExecucaoRepository.save(log);
        }
    }
}
