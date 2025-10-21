package co.edu.uniandes.proyecto1.domain.model.tiquete;

import java.math.BigDecimal;

public class PaqueteDeluxe extends TiqueteComposite {
    private final String descripcionBeneficios;
    private final boolean incluyeMercancia;

    public PaqueteDeluxe(String id, String localidadId, boolean transferible,
                         BigDecimal precioAgrupacion, int restriccionTopeX,
                         String descripcionBeneficios, boolean incluyeMercancia) {
        super(id, localidadId, transferible, precioAgrupacion, restriccionTopeX);
        this.descripcionBeneficios = descripcionBeneficios == null ? "" : descripcionBeneficios;
        this.incluyeMercancia = incluyeMercancia;
    }

    public String getDescripcionBeneficios() { return descripcionBeneficios; }
    public boolean isIncluyeMercancia() { return incluyeMercancia; }
}


