package co.edu.uniandes.proyecto1.domain.repository;

import co.edu.uniandes.proyecto1.domain.model.tiquete.Tiquete;

import java.util.List;
import java.util.Optional;

public interface TiqueteRepository {
    Optional<Tiquete> findById(String id);
    List<Tiquete> findAll();
    List<Tiquete> findByLocalidad(String localidadId);
    List<Tiquete> findByComprador(String compradorId);
    Tiquete save(Tiquete tiquete);
    void update(Tiquete tiquete);
}


