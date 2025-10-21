package co.edu.uniandes.proyecto1.infrastructure.csv.mappers;

import co.edu.uniandes.proyecto1.domain.model.oferta.Oferta;
import co.edu.uniandes.proyecto1.infrastructure.csv.CsvFormat;

import java.time.LocalDate;
import java.util.List;

public class OfertaCsvMapper implements CsvMapper<Oferta> {
    @Override
    public String toLine(Oferta o) {
        return CsvFormat.join(o.getId(), o.getLocalidadId(), Double.toString(o.getDescuentoPorcentual()),
                o.getInicio().toString(), o.getFin().toString());
    }

    @Override
    public Oferta fromLine(String line) {
        List<String> c = CsvFormat.split(line);
        return new Oferta(c.get(0), c.get(1), Double.parseDouble(c.get(2)), LocalDate.parse(c.get(3)), LocalDate.parse(c.get(4)));
    }
}


