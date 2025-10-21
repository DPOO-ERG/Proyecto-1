package co.edu.uniandes.proyecto1.application.service;

import co.edu.uniandes.proyecto1.domain.exception.BusinessException;
import co.edu.uniandes.proyecto1.domain.model.tiquete.Tiquete;
import co.edu.uniandes.proyecto1.domain.model.user.Usuario;
import co.edu.uniandes.proyecto1.domain.repository.TiqueteRepository;

public class TransferService {
    private final TiqueteRepository tiquetes;

    public TransferService(TiqueteRepository tiquetes) {
        this.tiquetes = tiquetes;
    }

    public void transferir(Usuario solicitante, String ticketId, String nuevoCompradorId) {
        Tiquete t = tiquetes.findById(ticketId).orElseThrow(() -> new BusinessException("Tiquete no existe"));
        if (t.getCompradorId() == null || !t.getCompradorId().equals(solicitante.getId())) {
            throw new BusinessException("Solo el propietario puede transferir");
        }
        t.transferirA(nuevoCompradorId);
        tiquetes.update(t);
    }
}


