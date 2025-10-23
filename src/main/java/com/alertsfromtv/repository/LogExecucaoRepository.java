package com.alertsfromtv.repository;

import com.alertsfromtv.entity.LogExecucao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface LogExecucaoRepository extends JpaRepository<LogExecucao, Long> {
    List<LogExecucao> findByConexaoIdOrderByTimestampDesc(UUID conexaoId, Pageable pageable);
}
