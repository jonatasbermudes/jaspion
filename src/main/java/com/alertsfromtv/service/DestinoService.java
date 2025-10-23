package com.alertsfromtv.service;

import com.alertsfromtv.dto.DestinoDto;
import com.alertsfromtv.dto.DestinoDto;
import com.alertsfromtv.entity.DestinoTelegram;
import com.alertsfromtv.entity.Usuario;
import com.alertsfromtv.exception.ResourceNotFoundException;
import com.alertsfromtv.repository.DestinoTelegramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinoService {

    @Autowired
    private DestinoTelegramRepository destinoRepository;

    public DestinoTelegram createDestino(DestinoDto destinoDto, Usuario usuario) {
        DestinoTelegram destino = new DestinoTelegram();
        destino.setNome(destinoDto.getNome());
        destino.setChatId(destinoDto.getChatId());
        destino.setUsuario(usuario);
        return destinoRepository.save(destino);
    }

    public List<DestinoTelegram> getDestinosByUsuario(Usuario usuario) {
        return destinoRepository.findByUsuarioId(usuario.getId());
    }

    public DestinoTelegram getDestinoByIdAndUsuario(Long id, Usuario usuario) {
        return destinoRepository.findById(id)
                .filter(destino -> destino.getUsuario().getId().equals(usuario.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Destino not found with id " + id));
    }

    public DestinoTelegram updateDestino(Long id, DestinoDto destinoDto, Usuario usuario) {
        DestinoTelegram destino = getDestinoByIdAndUsuario(id, usuario);
        destino.setNome(destinoDto.getNome());
        destino.setChatId(destinoDto.getChatId());
        return destinoRepository.save(destino);
    }

    public void deleteDestino(Long id, Usuario usuario) {
        DestinoTelegram destino = getDestinoByIdAndUsuario(id, usuario);
        destinoRepository.delete(destino);
    }
}
