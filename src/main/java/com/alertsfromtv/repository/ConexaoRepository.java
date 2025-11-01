package com.alertsfromtv.repository;

import com.alertsfromtv.entity.ConexaoWebhook;
import com.alertsfromtv.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConexaoRepository extends JpaRepository<ConexaoWebhook, UUID> {
    List<ConexaoWebhook> findByUsuario(Usuario usuario);
    Optional<ConexaoWebhook> findByIdAndUsuario(UUID id, Usuario usuario);
}
