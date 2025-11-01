package com.alertsfromtv.dto;

public class BotDto {
    private Long id;
    private String nome;
    private String tokenApi;

    public BotDto() {
    }

    public BotDto(Long id, String nome, String tokenApi) {
        this.id = id;
        this.nome = nome;
        this.tokenApi = tokenApi;
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
}
