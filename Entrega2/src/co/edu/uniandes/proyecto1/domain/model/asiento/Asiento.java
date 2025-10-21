package co.edu.uniandes.proyecto1.domain.model.asiento;

import java.util.Objects;

public class Asiento {
    private final String id;
    private final String localidadId;
    private final String numeroAsiento;
    private boolean disponible;

    public Asiento(String id, String localidadId, String numeroAsiento, boolean disponible) {
        this.id = Objects.requireNonNull(id);
        this.localidadId = Objects.requireNonNull(localidadId);
        this.numeroAsiento = Objects.requireNonNull(numeroAsiento);
        this.disponible = disponible;
    }

    public String getId() { return id; }
    public String getLocalidadId() { return localidadId; }
    public String getNumeroAsiento() { return numeroAsiento; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}


