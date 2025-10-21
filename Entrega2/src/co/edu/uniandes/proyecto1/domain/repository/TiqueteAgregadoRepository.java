package co.edu.uniandes.proyecto1.domain.repository;

import java.util.List;

public interface TiqueteAgregadoRepository {
    void addComponente(String compositeId, String tipoComposite, String componenteTicketId);
    List<String> findComponentes(String compositeId);
}


