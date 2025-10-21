package co.edu.uniandes.proyecto1.infrastructure.csv.repository;

import co.edu.uniandes.proyecto1.domain.model.venue.Venue;
import co.edu.uniandes.proyecto1.domain.repository.VenueRepository;
import co.edu.uniandes.proyecto1.infrastructure.csv.CsvPaths;
import co.edu.uniandes.proyecto1.infrastructure.csv.mappers.VenueCsvMapper;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class CsvVenueRepository extends AbstractCsvRepository<Venue> implements VenueRepository {
    public CsvVenueRepository() {
        super(resolvePath(), new VenueCsvMapper(),
                "id,nombre,ubicacion,capacidadMaxima,aprobado,organizadorId",
                Venue::getId);
    }

    private static Path resolvePath() { return CsvPaths.resolve("venues.csv"); }

    @Override
    public List<Venue> findByAprobado(boolean aprobado) {
        return findAll().stream().filter(v -> v.isAprobado() == aprobado).collect(Collectors.toList());
    }

    @Override
    public java.util.Optional<Venue> findByNombre(String nombre) {
        return findAll().stream().filter(v -> v.getNombre().equalsIgnoreCase(nombre)).findFirst();
    }
}


