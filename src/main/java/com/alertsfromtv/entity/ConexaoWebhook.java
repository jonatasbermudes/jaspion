package com.alertsfromtv.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "conexoes_webhook")
public class ConexaoWebhook {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    private boolean ativo;

    @Column(length = 2048)
    private String templateMensagem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bot_id", nullable = false)
    private BotTelegram bot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destino_id", nullable = false)
    private DestinoTelegram destino;

    public ConexaoWebhook() {
    }

    public ConexaoWebhook(String nome, boolean ativo, String templateMensagem, Usuario usuario, BotTelegram bot, DestinoTelegram destino) {
        this.nome = nome;
        this.ativo = ativo;
        this.templateMensagem = templateMensagem;
        this.usuario = usuario;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public BotTelegram getBot() {
        return bot;
    }

    public void setBot(BotTelegram bot) {
        this.bot = bot;
    }

    public DestinoTelegram getDestino() {
        return destino;
    }

    public void setDestino(DestinoTelegram destino) {
        this.destino = destino;
    }
}
