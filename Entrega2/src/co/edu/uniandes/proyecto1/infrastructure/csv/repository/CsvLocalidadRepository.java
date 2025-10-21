package co.edu.uniandes.proyecto1.infrastructure.csv.repository;

import co.edu.uniandes.proyecto1.domain.model.localidad.Localidad;
import co.edu.uniandes.proyecto1.domain.repository.LocalidadRepository;
import co.edu.uniandes.proyecto1.infrastructure.csv.CsvPaths;
import co.edu.uniandes.proyecto1.infrastructure.csv.mappers.LocalidadCsvMapper;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class CsvLocalidadRepository extends AbstractCsvRepository<Localidad> implements LocalidadRepository {
    public CsvLocalidadRepository() {
        super(resolvePath(), new LocalidadCsvMapper(),
                "id,eventoId,nombre,precioBase,esNumerada",
                Localidad::getId);
    }

    private static Path resolvePath() { return CsvPaths.resolve("localidades.csv"); }

    @Override
    public List<Localidad> findByEvento(String eventoId) {
        return findAll().stream().filter(l -> l.getEventoId().equals(eventoId)).collect(Collectors.toList());
    }

    @Override
    public java.util.Optional<Localidad> findByEventoAndNombre(String eventoId, String nombre) {
        return findAll().stream()
                .filter(l -> l.getEventoId().equals(eventoId) && l.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }
}


