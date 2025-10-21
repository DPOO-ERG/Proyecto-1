package co.edu.uniandes.proyecto1.infrastructure.csv.mappers;

import co.edu.uniandes.proyecto1.infrastructure.csv.CsvFormat;

public class TiqueteAgregadoCsvMapper {
    public String toLine(String compositeId, String tipoComposite, String componenteTicketId) {
        return CsvFormat.join(compositeId, tipoComposite, componenteTicketId);
    }
}


