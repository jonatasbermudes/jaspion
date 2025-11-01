package com.alertsfromtv.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "destinos_telegram")
public class DestinoTelegram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String chatId;
}
