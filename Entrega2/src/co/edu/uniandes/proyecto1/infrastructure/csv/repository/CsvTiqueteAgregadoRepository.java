package co.edu.uniandes.proyecto1.infrastructure.csv.repository;

import co.edu.uniandes.proyecto1.domain.repository.TiqueteAgregadoRepository;
import co.edu.uniandes.proyecto1.infrastructure.csv.CSVUtil;
import co.edu.uniandes.proyecto1.infrastructure.csv.CsvPaths;
import co.edu.uniandes.proyecto1.infrastructure.csv.mappers.TiqueteAgregadoCsvMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvTiqueteAgregadoRepository implements TiqueteAgregadoRepository {
    private final Path path;
    private final TiqueteAgregadoCsvMapper mapper = new TiqueteAgregadoCsvMapper();

    public CsvTiqueteAgregadoRepository() {
        this.path = CsvPaths.resolve("tiquetes_agregados.csv");
        ensureHeader();
    }

    @Override
    public void addComponente(String compositeId, String tipoComposite, String componenteTicketId) {
        try {
            List<String> lines = CSVUtil.readAll(path);
            if (lines.isEmpty()) {
                lines = new ArrayList<>();
                lines.add("compositeId,tipoComposite,componenteTicketId");
            }
            lines.add(mapper.toLine(compositeId, tipoComposite, componenteTicketId));
            CSVUtil.writeAllAtomic(path, lines);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> findComponentes(String compositeId) {
        try {
            List<String> lines = CSVUtil.readAll(path);
            List<String> out = new ArrayList<>();
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",", 3);
                if (parts.length >= 3 && parts[0].equals(compositeId)) {
                    out.add(parts[2]);
                }
            }
            return out;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void ensureHeader() {
        try {
            List<String> lines = CSVUtil.readAll(path);
            if (lines.isEmpty()) {
                lines.add("compositeId,tipoComposite,componenteTicketId");
                CSVUtil.writeAllAtomic(path, lines);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


