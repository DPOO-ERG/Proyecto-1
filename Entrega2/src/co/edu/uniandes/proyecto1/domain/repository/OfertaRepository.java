package co.edu.uniandes.proyecto1.domain.repository;

import co.edu.uniandes.proyecto1.domain.model.oferta.Oferta;

import java.util.List;
import java.util.Optional;

public interface OfertaRepository {
    Optional<Oferta> findById(String id);
    List<Oferta> findAll();
    List<Oferta> findByLocalidad(String localidadId);
    Oferta save(Oferta oferta);
    void update(Oferta oferta);
}


