package com.alertsfromtv.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class ConexaoResponseDto {
    private UUID id;
    private String nome;
    private boolean ativo;
    private String templateMensagem;
    private BotDto bot;
    private DestinoDto destino;
}
