package co.edu.uniandes.proyecto1.domain.model.tiquete;

import java.math.BigDecimal;
import java.util.Objects;

public abstract class Tiquete {
    private final String id;
    private final String localidadId;
    private final boolean transferible;
    private boolean vendido;
    private BigDecimal precioFinal;
    private BigDecimal cargoServicio;
    private BigDecimal cuotaEmision;
    private String compradorId; // null si no vendido

    protected Tiquete(String id, String localidadId, boolean transferible) {
        this.id = Objects.requireNonNull(id);
        this.localidadId = Objects.requireNonNull(localidadId);
        this.transferible = transferible;
        this.vendido = false;
        this.precioFinal = BigDecimal.ZERO;
        this.cargoServicio = BigDecimal.ZERO;
        this.cuotaEmision = BigDecimal.ZERO;
    }

    public String getId() { return id; }
    public String getLocalidadId() { return localidadId; }
    public boolean isTransferible() { return transferible; }
    public boolean isVendido() { return vendido; }
    public BigDecimal getPrecioFinal() { return precioFinal; }
    public BigDecimal getCargoServicio() { return cargoServicio; }
    public BigDecimal getCuotaEmision() { return cuotaEmision; }
    public String getCompradorId() { return compradorId; }

    public void marcarVendido(String compradorId, BigDecimal precioFinal, BigDecimal cargoServicio, BigDecimal cuotaEmision) {
        if (this.vendido) throw new IllegalStateException("El tiquete ya fue vendido");
        this.vendido = true;
        this.compradorId = Objects.requireNonNull(compradorId);
        this.precioFinal = precioFinal == null ? BigDecimal.ZERO : precioFinal;
        this.cargoServicio = cargoServicio == null ? BigDecimal.ZERO : cargoServicio;
        this.cuotaEmision = cuotaEmision == null ? BigDecimal.ZERO : cuotaEmision;
    }

    public void transferirA(String nuevoCompradorId) {
        if (!transferible) throw new IllegalStateException("El tiquete no es transferible");
        if (!vendido) throw new IllegalStateException("El tiquete no ha sido vendido");
        this.compradorId = Objects.requireNonNull(nuevoCompradorId);
    }
}


