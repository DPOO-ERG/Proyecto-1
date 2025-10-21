package co.edu.uniandes.proyecto1.infrastructure.csv.repository;

import co.edu.uniandes.proyecto1.domain.model.asiento.Asiento;
import co.edu.uniandes.proyecto1.domain.repository.AsientoRepository;
import co.edu.uniandes.proyecto1.infrastructure.csv.CsvPaths;
import co.edu.uniandes.proyecto1.infrastructure.csv.mappers.AsientoCsvMapper;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class CsvAsientoRepository extends AbstractCsvRepository<Asiento> implements AsientoRepository {
    public CsvAsientoRepository() {
        super(resolvePath(), new AsientoCsvMapper(),
                "id,localidadId,numeroAsiento,disponible",
                Asiento::getId);
    }

    private static Path resolvePath() { return CsvPaths.resolve("asientos.csv"); }

    @Override
    public List<Asiento> findByLocalidad(String localidadId) {
        return findAll().stream().filter(a -> a.getLocalidadId().equals(localidadId)).collect(Collectors.toList());
    }

    @Override
    public java.util.Optional<Asiento> findByLocalidadAndNumero(String localidadId, String numeroAsiento) {
        return findAll().stream()
                .filter(a -> a.getLocalidadId().equals(localidadId) && a.getNumeroAsiento().equalsIgnoreCase(numeroAsiento))
                .findFirst();
    }
}


