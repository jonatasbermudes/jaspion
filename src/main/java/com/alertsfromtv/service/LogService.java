package com.alertsfromtv.service;

import com.alertsfromtv.entity.ConexaoWebhook;
import com.alertsfromtv.entity.ConexaoWebhook;
import com.alertsfromtv.entity.LogExecucao;
import com.alertsfromtv.entity.Usuario;
import com.alertsfromtv.exception.ResourceNotFoundException;
import com.alertsfromtv.repository.ConexaoWebhookRepository;
import com.alertsfromtv.repository.LogExecucaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LogService {

    @Autowired
    private LogExecucaoRepository logRepository;

    @Autowired
    private ConexaoWebhookRepository conexaoRepository;

    public List<LogExecucao> getLogsByConexaoAndUsuario(UUID conexaoId, Usuario usuario, Pageable pageable) {
        ConexaoWebhook conexao = conexaoRepository.findById(conexaoId)
                .filter(c -> c.getUsuario().getId().equals(usuario.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Conexao not found with id " + conexaoId));

        return logRepository.findByConexaoIdOrderByTimestampDesc(conexao.getId(), pageable);
    }
}
