package co.edu.uniandes.proyecto1.domain.model.evento;

import java.time.LocalDate;
import java.util.Objects;

public class Evento {
    private final String id;
    private String nombre;
    private LocalDate fecha;
    private String tipoEvento;
    private final String venueId;
    private final String organizadorId;

    public Evento(String id, String nombre, LocalDate fecha, String tipoEvento,
                  String venueId, String organizadorId) {
        this.id = Objects.requireNonNull(id);
        this.nombre = Objects.requireNonNull(nombre);
        this.fecha = Objects.requireNonNull(fecha);
        this.tipoEvento = Objects.requireNonNull(tipoEvento);
        this.venueId = Objects.requireNonNull(venueId);
        this.organizadorId = Objects.requireNonNull(organizadorId);
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public LocalDate getFecha() { return fecha; }
    public String getTipoEvento() { return tipoEvento; }
    public String getVenueId() { return venueId; }
    public String getOrganizadorId() { return organizadorId; }

    public void setNombre(String nombre) { this.nombre = Objects.requireNonNull(nombre); }
    public void setFecha(LocalDate fecha) { this.fecha = Objects.requireNonNull(fecha); }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = Objects.requireNonNull(tipoEvento); }
}


