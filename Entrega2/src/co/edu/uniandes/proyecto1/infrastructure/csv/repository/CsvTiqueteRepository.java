package co.edu.uniandes.proyecto1.infrastructure.csv.repository;

import co.edu.uniandes.proyecto1.domain.model.tiquete.Tiquete;
import co.edu.uniandes.proyecto1.domain.repository.TiqueteRepository;
import co.edu.uniandes.proyecto1.infrastructure.csv.CsvPaths;
import co.edu.uniandes.proyecto1.infrastructure.csv.mappers.TiqueteCsvMapper;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class CsvTiqueteRepository extends AbstractCsvRepository<Tiquete> implements TiqueteRepository {
    public CsvTiqueteRepository() {
        super(resolvePath(), new TiqueteCsvMapper(),
                "id,tipo,localidadId,compradorId,precioFinal,cargoServicio,cuotaEmision,transferible,vendido,numeroAsiento,precioAgrupacion,restriccionTopeX",
                Tiquete::getId);
    }

    private static Path resolvePath() { return CsvPaths.resolve("tiquetes.csv"); }

    @Override
    public List<Tiquete> findByLocalidad(String localidadId) {
        return findAll().stream().filter(t -> t.getLocalidadId().equals(localidadId)).collect(Collectors.toList());
    }

    @Override
    public List<Tiquete> findByComprador(String compradorId) {
        return findAll().stream().filter(t -> compradorId.equals(t.getCompradorId())).collect(Collectors.toList());
    }
}


