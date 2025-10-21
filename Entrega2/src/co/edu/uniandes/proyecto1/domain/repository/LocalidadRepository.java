package co.edu.uniandes.proyecto1.domain.repository;

import co.edu.uniandes.proyecto1.domain.model.localidad.Localidad;

import java.util.List;
import java.util.Optional;

public interface LocalidadRepository {
    Optional<Localidad> findById(String id);
    List<Localidad> findAll();
    List<Localidad> findByEvento(String eventoId);
    Optional<Localidad> findByEventoAndNombre(String eventoId, String nombre);
    Localidad save(Localidad localidad);
    void update(Localidad localidad);
}


