package co.edu.uniandes.proyecto1.domain.model.tiquete;

import java.math.BigDecimal;

public class TiqueteMultiple extends TiqueteComposite {
    public TiqueteMultiple(String id, String localidadId, boolean transferible,
                           BigDecimal precioAgrupacion, int restriccionTopeX) {
        super(id, localidadId, transferible, precioAgrupacion, restriccionTopeX);
    }
}


