package co.edu.uniandes.proyecto1.infrastructure.csv.repository;

import co.edu.uniandes.proyecto1.domain.model.oferta.Oferta;
import co.edu.uniandes.proyecto1.domain.repository.OfertaRepository;
import co.edu.uniandes.proyecto1.infrastructure.csv.CsvPaths;
import co.edu.uniandes.proyecto1.infrastructure.csv.mappers.OfertaCsvMapper;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class CsvOfertaRepository extends AbstractCsvRepository<Oferta> implements OfertaRepository {
    public CsvOfertaRepository() {
        super(resolvePath(), new OfertaCsvMapper(),
                "id,localidadId,descuentoPorcentual,inicioISO,finISO",
                Oferta::getId);
    }

    private static Path resolvePath() { return CsvPaths.resolve("ofertas.csv"); }

    @Override
    public List<Oferta> findByLocalidad(String localidadId) {
        return findAll().stream().filter(o -> o.getLocalidadId().equals(localidadId)).collect(Collectors.toList());
    }
}


