package com.alertsfromtv.repository;

import com.alertsfromtv.entity.ConexaoWebhook;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ConexaoWebhookRepository extends JpaRepository<ConexaoWebhook, UUID> {
    List<ConexaoWebhook> findByUsuarioId(Long usuarioId);
}
