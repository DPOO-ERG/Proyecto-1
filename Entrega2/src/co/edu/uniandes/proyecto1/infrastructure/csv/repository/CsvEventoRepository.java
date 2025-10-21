package co.edu.uniandes.proyecto1.infrastructure.csv.repository;

import co.edu.uniandes.proyecto1.domain.model.evento.Evento;
import co.edu.uniandes.proyecto1.domain.repository.EventoRepository;
import co.edu.uniandes.proyecto1.infrastructure.csv.CsvPaths;
import co.edu.uniandes.proyecto1.infrastructure.csv.mappers.EventoCsvMapper;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class CsvEventoRepository extends AbstractCsvRepository<Evento> implements EventoRepository {
    public CsvEventoRepository() {
        super(resolvePath(), new EventoCsvMapper(),
                "id,nombre,fechaISO,tipoEvento,venueId,organizadorId",
                Evento::getId);
    }

    private static Path resolvePath() { return CsvPaths.resolve("eventos.csv"); }

    @Override
    public List<Evento> findByVenue(String venueId) {
        return findAll().stream().filter(e -> e.getVenueId().equals(venueId)).collect(Collectors.toList());
    }

    @Override
    public List<Evento> findByOrganizador(String organizadorId) {
        return findAll().stream().filter(e -> e.getOrganizadorId().equals(organizadorId)).collect(Collectors.toList());
    }

    @Override
    public java.util.Optional<Evento> findByVenueAndNombre(String venueId, String nombre) {
        return findAll().stream()
                .filter(e -> e.getVenueId().equals(venueId) && e.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }
}


