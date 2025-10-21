package co.edu.uniandes.proyecto1.domain.model.localidad;

import java.math.BigDecimal;
import java.util.Objects;

public class Localidad {
    private final String id;
    private final String eventoId;
    private String nombre;
    private BigDecimal precioBase;
    private boolean esNumerada;

    public Localidad(String id, String eventoId, String nombre, BigDecimal precioBase, boolean esNumerada) {
        this.id = Objects.requireNonNull(id);
        this.eventoId = Objects.requireNonNull(eventoId);
        this.nombre = Objects.requireNonNull(nombre);
        this.precioBase = precioBase == null ? BigDecimal.ZERO : precioBase;
        this.esNumerada = esNumerada;
    }

    public String getId() { return id; }
    public String getEventoId() { return eventoId; }
    public String getNombre() { return nombre; }
    public BigDecimal getPrecioBase() { return precioBase; }
    public boolean isEsNumerada() { return esNumerada; }

    public void setNombre(String nombre) { this.nombre = Objects.requireNonNull(nombre); }
    public void setPrecioBase(BigDecimal precioBase) { this.precioBase = precioBase == null ? BigDecimal.ZERO : precioBase; }
    public void setEsNumerada(boolean esNumerada) { this.esNumerada = esNumerada; }
}


