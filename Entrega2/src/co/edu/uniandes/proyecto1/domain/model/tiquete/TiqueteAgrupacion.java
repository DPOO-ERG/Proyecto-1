package co.edu.uniandes.proyecto1.domain.model.tiquete;

import java.math.BigDecimal;

public class TiqueteAgrupacion extends TiqueteBasico {
    private final BigDecimal precioAgrupacion;
    private final int restriccionTopeX;

    public TiqueteAgrupacion(String id, String localidadId, boolean transferible,
                             BigDecimal precioAgrupacion, int restriccionTopeX) {
        super(id, localidadId, transferible);
        this.precioAgrupacion = precioAgrupacion == null ? BigDecimal.ZERO : precioAgrupacion;
        this.restriccionTopeX = Math.max(restriccionTopeX, 0);
    }

    public BigDecimal getPrecioAgrupacion() { return precioAgrupacion; }
    public int getRestriccionTopeX() { return restriccionTopeX; }
}


