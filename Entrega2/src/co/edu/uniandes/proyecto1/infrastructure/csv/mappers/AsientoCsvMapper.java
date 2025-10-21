package co.edu.uniandes.proyecto1.infrastructure.csv.mappers;

import co.edu.uniandes.proyecto1.domain.model.asiento.Asiento;
import co.edu.uniandes.proyecto1.infrastructure.csv.CsvFormat;

import java.util.List;

public class AsientoCsvMapper implements CsvMapper<Asiento> {
    @Override
    public String toLine(Asiento a) {
        return CsvFormat.join(a.getId(), a.getLocalidadId(), a.getNumeroAsiento(), Boolean.toString(a.isDisponible()));
    }

    @Override
    public Asiento fromLine(String line) {
        List<String> c = CsvFormat.split(line);
        return new Asiento(c.get(0), c.get(1), c.get(2), Boolean.parseBoolean(c.get(3)));
    }
}


