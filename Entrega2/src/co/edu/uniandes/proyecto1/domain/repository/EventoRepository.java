package co.edu.uniandes.proyecto1.domain.repository;

import co.edu.uniandes.proyecto1.domain.model.evento.Evento;

import java.util.List;
import java.util.Optional;

public interface EventoRepository {
    Optional<Evento> findById(String id);
    List<Evento> findAll();
    List<Evento> findByVenue(String venueId);
    List<Evento> findByOrganizador(String organizadorId);
    Optional<Evento> findByVenueAndNombre(String venueId, String nombre);
    Evento save(Evento evento);
    void update(Evento evento);
}


