package co.edu.uniandes.proyecto1.domain.repository;

import co.edu.uniandes.proyecto1.domain.model.venue.Venue;

import java.util.List;
import java.util.Optional;

public interface VenueRepository {
    Optional<Venue> findById(String id);
    List<Venue> findAll();
    List<Venue> findByAprobado(boolean aprobado);
    Optional<Venue> findByNombre(String nombre);
    Venue save(Venue venue);
    void update(Venue venue);
}


