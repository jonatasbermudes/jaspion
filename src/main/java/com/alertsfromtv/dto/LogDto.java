package com.alertsfromtv.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class LogDto {
    private Long id;
    private Instant timestamp;
    private boolean sucesso;
    private String bodyRecebido;
    private String respostaTelegram;
}
