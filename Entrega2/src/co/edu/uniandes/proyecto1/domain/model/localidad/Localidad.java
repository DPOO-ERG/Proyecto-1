package co.edu.uniandes.proyecto1.domain.model.localidad;

import java.math.BigDecimal;
import java.util.Objects;

public class Localidad {
    private final String id;
    private final String eventoId;
    private String nombre;
    private BigDecimal precioBase;
    private boolean esNumerada;
    private int capacidad; // Aplica para no numeradas

    public Localidad(String id, String eventoId, String nombre, BigDecimal precioBase, boolean esNumerada, int capacidad) {
        this.id = Objects.requireNonNull(id);
        this.eventoId = Objects.requireNonNull(eventoId);
        this.nombre = Objects.requireNonNull(nombre);
        this.precioBase = precioBase == null ? BigDecimal.ZERO : precioBase;
        this.esNumerada = esNumerada;
        this.capacidad = Math.max(0, capacidad);
    }

    public String getId() { return id; }
    public String getEventoId() { return eventoId; }
    public String getNombre() { return nombre; }
    public BigDecimal getPrecioBase() { return precioBase; }
    public boolean isEsNumerada() { return esNumerada; }
    public int getCapacidad() { return capacidad; }

    public void setNombre(String nombre) { this.nombre = Objects.requireNonNull(nombre); }
    public void setPrecioBase(BigDecimal precioBase) { this.precioBase = precioBase == null ? BigDecimal.ZERO : precioBase; }
    public void setEsNumerada(boolean esNumerada) { this.esNumerada = esNumerada; }
    public void setCapacidad(int capacidad) { this.capacidad = Math.max(0, capacidad); }
}


