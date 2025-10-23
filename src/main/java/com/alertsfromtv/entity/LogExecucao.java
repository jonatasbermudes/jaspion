package com.alertsfromtv.entity;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import java.time.Instant;

@Data
@Entity
@Table(name = "logs_execucao")
public class LogExecucao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant timestamp;
    private boolean sucesso;

    @Lob
    @Column(name = "body_recebido")
    private String bodyRecebido;

    @Lob
    @Column(name = "resposta_telegram")
    private String respostaTelegram;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conexao_id", nullable = false)
    private ConexaoWebhook conexao;
}
