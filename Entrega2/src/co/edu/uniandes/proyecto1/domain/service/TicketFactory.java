package co.edu.uniandes.proyecto1.domain.service;

import co.edu.uniandes.proyecto1.domain.model.tiquete.*;

import java.math.BigDecimal;

public final class TicketFactory {
    private TicketFactory() { }

    public static TiqueteBasico crearBasico(String localidadId, boolean transferible) {
        return new TiqueteBasico(IdGenerator.newId(), localidadId, transferible);
    }

    public static TiqueteNumerado crearNumerado(String localidadId, boolean transferible, String numeroAsiento) {
        return new TiqueteNumerado(IdGenerator.newId(), localidadId, transferible, numeroAsiento);
    }

    public static TiqueteAgrupacion crearAgrupacion(String localidadId, boolean transferible,
                                                     BigDecimal precioAgrupacion, int restriccionTopeX) {
        return new TiqueteAgrupacion(IdGenerator.newId(), localidadId, transferible, precioAgrupacion, restriccionTopeX);
    }

    public static TiqueteMultiple crearMultiple(String localidadId, boolean transferible,
                                                BigDecimal precioAgrupacion, int restriccionTopeX) {
        return new TiqueteMultiple(IdGenerator.newId(), localidadId, transferible, precioAgrupacion, restriccionTopeX);
    }

    public static PaqueteDeluxe crearPaqueteDeluxe(String localidadId, boolean transferible,
                                                   BigDecimal precioAgrupacion, int restriccionTopeX,
                                                   String descripcionBeneficios, boolean incluyeMercancia) {
        return new PaqueteDeluxe(IdGenerator.newId(), localidadId, transferible, precioAgrupacion, restriccionTopeX,
                descripcionBeneficios, incluyeMercancia);
    }
}


