package co.edu.uniandes.proyecto1.infrastructure.csv.repository;

import co.edu.uniandes.proyecto1.infrastructure.csv.CSVUtil;
import co.edu.uniandes.proyecto1.infrastructure.csv.mappers.CsvMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractCsvRepository<T> {
    private final Path path;
    private final CsvMapper<T> mapper;
    private final String header;
    private final Function<T, String> idExtractor;

    protected AbstractCsvRepository(Path path, CsvMapper<T> mapper, String header, Function<T, String> idExtractor) {
        this.path = Objects.requireNonNull(path);
        this.mapper = Objects.requireNonNull(mapper);
        this.header = Objects.requireNonNull(header);
        this.idExtractor = Objects.requireNonNull(idExtractor);
        ensureHeader();
    }

    protected List<T> readAll() {
        try {
            List<String> lines = CSVUtil.readAll(path);
            if (lines.isEmpty()) return new ArrayList<>();
            if (!lines.isEmpty() && lines.get(0).startsWith(firstHeaderToken())) {
                lines = lines.subList(1, lines.size());
            }
            List<T> values = new ArrayList<>();
            for (String line : lines) {
                if (line == null || line.isBlank()) continue;
                values.add(mapper.fromLine(line));
            }
            return values;
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo " + path + ": " + e.getMessage(), e);
        }
    }

    protected void writeAll(List<T> values) {
        List<String> lines = new ArrayList<>();
        lines.add(header);
        lines.addAll(values.stream().map(mapper::toLine).collect(Collectors.toList()));
        try {
            CSVUtil.writeAllAtomic(path, lines);
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo " + path + ": " + e.getMessage(), e);
        }
    }

    public List<T> findAll() { return readAll(); }

    public Optional<T> findById(String id) {
        return readAll().stream().filter(v -> idExtractor.apply(v).equals(id)).findFirst();
    }

    public T save(T value) {
        List<T> all = readAll();
        String id = idExtractor.apply(value);
        boolean exists = all.stream().anyMatch(v -> idExtractor.apply(v).equals(id));
        if (exists) throw new IllegalStateException("El id ya existe: " + id);
        all.add(value);
        writeAll(all);
        return value;
    }

    public void update(T value) {
        List<T> all = readAll();
        String id = idExtractor.apply(value);
        boolean replaced = false;
        for (int i = 0; i < all.size(); i++) {
            if (idExtractor.apply(all.get(i)).equals(id)) {
                all.set(i, value);
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            all.add(value);
        }
        writeAll(all);
    }

    private void ensureHeader() {
        try {
            if (!Files.exists(path)) {
                List<String> lines = new ArrayList<>();
                lines.add(header);
                CSVUtil.writeAllAtomic(path, lines);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear archivo: " + path, e);
        }
    }

    private String firstHeaderToken() {
        int idx = header.indexOf(',');
        return idx > 0 ? header.substring(0, idx) : header;
    }
}


