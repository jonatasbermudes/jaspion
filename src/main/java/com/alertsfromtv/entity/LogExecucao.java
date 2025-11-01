package com.alertsfromtv.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Data
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
}
