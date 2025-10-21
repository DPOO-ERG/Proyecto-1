package co.edu.uniandes.proyecto1.infrastructure.csv.mappers;

import co.edu.uniandes.proyecto1.domain.model.evento.Evento;
import co.edu.uniandes.proyecto1.infrastructure.csv.CsvFormat;

import java.time.LocalDate;
import java.util.List;

public class EventoCsvMapper implements CsvMapper<Evento> {
    @Override
    public String toLine(Evento e) {
        return CsvFormat.join(
                e.getId(), e.getNombre(), e.getFecha().toString(), e.getTipoEvento(), e.getVenueId(), e.getOrganizadorId()
        );
    }

    @Override
    public Evento fromLine(String line) {
        List<String> c = CsvFormat.split(line);
        return new Evento(c.get(0), c.get(1), LocalDate.parse(c.get(2)), c.get(3), c.get(4), c.get(5));
    }
}


