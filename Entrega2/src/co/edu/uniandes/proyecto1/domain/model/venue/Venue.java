package co.edu.uniandes.proyecto1.domain.model.venue;

import java.util.Objects;

public class Venue {
    private final String id;
    private String nombre;
    private String ubicacion;
    private int capacidadMaxima;
    private boolean aprobado;
    private final String organizadorId;

    public Venue(String id, String nombre, String ubicacion, int capacidadMaxima,
                 boolean aprobado, String organizadorId) {
        this.id = Objects.requireNonNull(id);
        this.nombre = Objects.requireNonNull(nombre);
        this.ubicacion = Objects.requireNonNull(ubicacion);
        this.capacidadMaxima = capacidadMaxima;
        this.aprobado = aprobado;
        this.organizadorId = Objects.requireNonNull(organizadorId);
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getUbicacion() { return ubicacion; }
    public int getCapacidadMaxima() { return capacidadMaxima; }
    public boolean isAprobado() { return aprobado; }
    public String getOrganizadorId() { return organizadorId; }

    public void setNombre(String nombre) { this.nombre = Objects.requireNonNull(nombre); }
    public void setUbicacion(String ubicacion) { this.ubicacion = Objects.requireNonNull(ubicacion); }
    public void setCapacidadMaxima(int capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }
    public void setAprobado(boolean aprobado) { this.aprobado = aprobado; }
}


