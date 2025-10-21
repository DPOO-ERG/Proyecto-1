package co.edu.uniandes.proyecto1.domain.repository;

import co.edu.uniandes.proyecto1.domain.model.asiento.Asiento;

import java.util.List;
import java.util.Optional;

public interface AsientoRepository {
    Optional<Asiento> findById(String id);
    List<Asiento> findByLocalidad(String localidadId);
    Optional<Asiento> findByLocalidadAndNumero(String localidadId, String numeroAsiento);
    Asiento save(Asiento asiento);
    void update(Asiento asiento);
}


