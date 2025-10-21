package co.edu.uniandes.proyecto1.infrastructure.csv.mappers;

import co.edu.uniandes.proyecto1.domain.model.venue.Venue;
import co.edu.uniandes.proyecto1.infrastructure.csv.CsvFormat;

import java.util.List;

public class VenueCsvMapper implements CsvMapper<Venue> {
    @Override
    public String toLine(Venue v) {
        return CsvFormat.join(
                v.getId(),
                v.getNombre(),
                v.getUbicacion(),
                Integer.toString(v.getCapacidadMaxima()),
                Boolean.toString(v.isAprobado()),
                v.getOrganizadorId()
        );
    }

    @Override
    public Venue fromLine(String line) {
        List<String> c = CsvFormat.split(line);
        return new Venue(
                c.get(0), c.get(1), c.get(2), Integer.parseInt(c.get(3)), Boolean.parseBoolean(c.get(4)), c.get(5)
        );
    }
}


