package com.alertsfromtv.dto;

import java.time.Instant;

public class LogDto {
    private Long id;
    private Instant timestamp;
    private boolean sucesso;
    private String bodyRecebido;
    private String respostaTelegram;

    public LogDto() {
    }

    public LogDto(Long id, Instant timestamp, boolean sucesso, String bodyRecebido, String respostaTelegram) {
        this.id = id;
        this.timestamp = timestamp;
        this.sucesso = sucesso;
        this.bodyRecebido = bodyRecebido;
        this.respostaTelegram = respostaTelegram;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }

    public String getBodyRecebido() {
        return bodyRecebido;
    }

    public void setBodyRecebido(String bodyRecebido) {
        this.bodyRecebido = bodyRecebido;
    }

    public String getRespostaTelegram() {
        return respostaTelegram;
    }

    public void setRespostaTelegram(String respostaTelegram) {
        this.respostaTelegram = respostaTelegram;
    }
}
