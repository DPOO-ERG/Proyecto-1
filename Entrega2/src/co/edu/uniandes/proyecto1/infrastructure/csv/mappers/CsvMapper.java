package co.edu.uniandes.proyecto1.infrastructure.csv.mappers;

public interface CsvMapper<T> {
    String toLine(T value);
    T fromLine(String line);
}


