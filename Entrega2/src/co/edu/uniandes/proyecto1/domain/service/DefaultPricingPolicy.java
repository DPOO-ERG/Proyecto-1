package co.edu.uniandes.proyecto1.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DefaultPricingPolicy implements PricingPolicy {
    @Override
    public PriceBreakdown calcularPrecio(BigDecimal precioBase, double mejorDescuento,
                                         BigDecimal porcentajeCargoServicio, BigDecimal cuotaEmision) {
        if (precioBase == null) precioBase = BigDecimal.ZERO;
        if (porcentajeCargoServicio == null) porcentajeCargoServicio = BigDecimal.ZERO;
        if (cuotaEmision == null) cuotaEmision = BigDecimal.ZERO;
        BigDecimal oneMinus = BigDecimal.ONE.subtract(BigDecimal.valueOf(mejorDescuento));
        BigDecimal subtotal = precioBase.multiply(oneMinus).setScale(2, RoundingMode.HALF_UP);
        BigDecimal cargo = subtotal.multiply(porcentajeCargoServicio).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(cargo).add(cuotaEmision).setScale(2, RoundingMode.HALF_UP);
        return new PriceBreakdown(subtotal, cargo, cuotaEmision, total, mejorDescuento);
    }
}


