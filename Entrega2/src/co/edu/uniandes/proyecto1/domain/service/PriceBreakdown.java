package co.edu.uniandes.proyecto1.domain.service;

import java.math.BigDecimal;

public final class PriceBreakdown {
    private final BigDecimal subtotal;
    private final BigDecimal cargoServicio;
    private final BigDecimal cuotaEmision;
    private final BigDecimal total;
    private final double descuentoAplicado; // 0..1

    public PriceBreakdown(BigDecimal subtotal, BigDecimal cargoServicio,
                          BigDecimal cuotaEmision, BigDecimal total, double descuentoAplicado) {
        this.subtotal = subtotal;
        this.cargoServicio = cargoServicio;
        this.cuotaEmision = cuotaEmision;
        this.total = total;
        this.descuentoAplicado = descuentoAplicado;
    }

    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getCargoServicio() { return cargoServicio; }
    public BigDecimal getCuotaEmision() { return cuotaEmision; }
    public BigDecimal getTotal() { return total; }
    public double getDescuentoAplicado() { return descuentoAplicado; }
}


