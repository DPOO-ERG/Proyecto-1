package co.edu.uniandes.proyecto1.domain.model.tiquete;

public class TiqueteNumerado extends TiqueteBasico {
    private final String numeroAsiento;

    public TiqueteNumerado(String id, String localidadId, boolean transferible, String numeroAsiento) {
        super(id, localidadId, transferible);
        this.numeroAsiento = numeroAsiento;
    }

    public String getNumeroAsiento() { return numeroAsiento; }
}


