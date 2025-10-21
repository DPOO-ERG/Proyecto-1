package co.edu.uniandes.proyecto1.domain.service;

import java.math.BigDecimal;

public interface PricingPolicy {
    PriceBreakdown calcularPrecio(BigDecimal precioBase, double mejorDescuento,
                                  BigDecimal porcentajeCargoServicio, BigDecimal cuotaEmision);
}


