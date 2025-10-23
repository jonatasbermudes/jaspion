package com.alertsfromtv.repository;

import com.alertsfromtv.entity.BotTelegram;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BotTelegramRepository extends JpaRepository<BotTelegram, Long> {
    List<BotTelegram> findByUsuarioId(Long usuarioId);
}
