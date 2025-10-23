package com.alertsfromtv.repository;

import com.alertsfromtv.entity.DestinoTelegram;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DestinoTelegramRepository extends JpaRepository<DestinoTelegram, Long> {
    List<DestinoTelegram> findByUsuarioId(Long usuarioId);
}
