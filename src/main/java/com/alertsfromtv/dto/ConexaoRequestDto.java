package com.alertsfromtv.dto;

public class ConexaoRequestDto {
    private String nome;
    private boolean ativo;
    private String templateMensagem;
    private Long botId;
    private Long destinoId;

    public ConexaoRequestDto() {
    }

    public ConexaoRequestDto(String nome, boolean ativo, String templateMensagem, Long botId, Long destinoId) {
        this.nome = nome;
        this.ativo = ativo;
        this.templateMensagem = templateMensagem;
        this.botId = botId;
        this.destinoId = destinoId;
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

    public Long getBotId() {
        return botId;
    }

    public void setBotId(Long botId) {
        this.botId = botId;
    }

    public Long getDestinoId() {
        return destinoId;
    }

    public void setDestinoId(Long destinoId) {
        this.destinoId = destinoId;
    }
}
