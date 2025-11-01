package com.alertsfromtv.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "bots_telegram")
public class BotTelegram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 1024, nullable = false)
    @Convert(converter = com.alertsfromtv.util.TokenCriptografiaConverter.class)
    private String tokenApi; // Token a ser criptografado

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public BotTelegram() {
    }

    public BotTelegram(String nome, String tokenApi, Usuario usuario) {
        this.nome = nome;
        this.tokenApi = tokenApi;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTokenApi() {
        return tokenApi;
    }

    public void setTokenApi(String tokenApi) {
        this.tokenApi = tokenApi;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
