package com.alertsfromtv.dto;

import lombok.Data;

@Data
public class ConexaoRequestDto {
    private String nome;
    private boolean ativo;
    private String templateMensagem;
    private Long botId;
    private Long destinoId;
}
