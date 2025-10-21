package co.edu.uniandes.proyecto1.infrastructure.csv.mappers;

import co.edu.uniandes.proyecto1.domain.model.localidad.Localidad;
import co.edu.uniandes.proyecto1.infrastructure.csv.CsvFormat;

import java.math.BigDecimal;
import java.util.List;

public class LocalidadCsvMapper implements CsvMapper<Localidad> {
    @Override
    public String toLine(Localidad l) {
        return CsvFormat.join(
                l.getId(), l.getEventoId(), l.getNombre(), l.getPrecioBase().toPlainString(), Boolean.toString(l.isEsNumerada())
        );
    }

    @Override
    public Localidad fromLine(String line) {
        List<String> c = CsvFormat.split(line);
        return new Localidad(c.get(0), c.get(1), c.get(2), new BigDecimal(c.get(3)), Boolean.parseBoolean(c.get(4)));
    }
}


