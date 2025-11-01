package com.alertsfromtv.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "logs_execucao")
public class LogExecucao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private Instant timestamp;

    private boolean sucesso;

    @Lob
    private String bodyRecebido;

    @Lob
    private String respostaTelegram;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conexao_id", nullable = false)
    private ConexaoWebhook conexao;

    public LogExecucao() {
    }

    public LogExecucao(Instant timestamp, boolean sucesso, String bodyRecebido, String respostaTelegram, ConexaoWebhook conexao) {
        this.timestamp = timestamp;
        this.sucesso = sucesso;
        this.bodyRecebido = bodyRecebido;
        this.respostaTelegram = respostaTelegram;
        this.conexao = conexao;
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

    public ConexaoWebhook getConexao() {
        return conexao;
    }

    public void setConexao(ConexaoWebhook conexao) {
        this.conexao = conexao;
    }
}
