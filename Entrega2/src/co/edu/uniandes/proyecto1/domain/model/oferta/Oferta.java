package co.edu.uniandes.proyecto1.domain.model.oferta;

import java.time.LocalDate;
import java.util.Objects;

public class Oferta {
    private final String id;
    private final String localidadId;
    private final double descuentoPorcentual; // 0..1
    private final LocalDate inicio;
    private final LocalDate fin;

    public Oferta(String id, String localidadId, double descuentoPorcentual, LocalDate inicio, LocalDate fin) {
        this.id = Objects.requireNonNull(id);
        this.localidadId = Objects.requireNonNull(localidadId);
        this.descuentoPorcentual = descuentoPorcentual;
        this.inicio = Objects.requireNonNull(inicio);
        this.fin = Objects.requireNonNull(fin);
    }

    public String getId() { return id; }
    public String getLocalidadId() { return localidadId; }
    public double getDescuentoPorcentual() { return descuentoPorcentual; }
    public LocalDate getInicio() { return inicio; }
    public LocalDate getFin() { return fin; }

    public boolean activaEn(LocalDate fecha) {
        return (fecha.isEqual(inicio) || fecha.isAfter(inicio)) && (fecha.isEqual(fin) || fecha.isBefore(fin));
    }
}


