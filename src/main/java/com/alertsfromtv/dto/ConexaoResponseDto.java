package com.alertsfromtv.dto;

import java.util.UUID;

public class ConexaoResponseDto {
    private UUID id;
    private String nome;
    private boolean ativo;
    private String templateMensagem;
    private BotDto bot;
    private DestinoDto destino;

    public ConexaoResponseDto() {
    }

    public ConexaoResponseDto(UUID id, String nome, boolean ativo, String templateMensagem, BotDto bot, DestinoDto destino) {
        this.id = id;
        this.nome = nome;
        this.ativo = ativo;
        this.templateMensagem = templateMensagem;
        this.bot = bot;
        this.destino = destino;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getTemplateMensagem() {
        return templateMensagem;
    }

    public void setTemplateMensagem(String templateMensagem) {
        this.templateMensagem = templateMensagem;
    }

    public BotDto getBot() {
        return bot;
    }

    public void setBot(BotDto bot) {
        this.bot = bot;
    }

    public DestinoDto getDestino() {
        return destino;
    }

    public void setDestino(DestinoDto destino) {
        this.destino = destino;
    }
}
