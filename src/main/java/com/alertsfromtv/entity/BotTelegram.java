package com.alertsfromtv.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
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
}
