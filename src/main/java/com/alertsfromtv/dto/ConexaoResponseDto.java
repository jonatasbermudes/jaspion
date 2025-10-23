package com.alertsfromtv.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class ConexaoResponseDto {
    private UUID id;
    private String nome;
    private boolean ativo;
    private String templateMensagem;
    private Long botId;
    private Long destinoId;
}
