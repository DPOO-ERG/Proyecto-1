package co.edu.uniandes.proyecto1.domain.model.user;

import java.math.BigDecimal;

public class Administrador extends Usuario {
    private BigDecimal porcentajeCargoServicio; // 0..1 (por ejemplo 0.1 = 10%)
    private BigDecimal cobroFijoEmision;        // valor fijo

    public Administrador(String id, String login, String password,
                         BigDecimal porcentajeCargoServicio, BigDecimal cobroFijoEmision) {
        super(id, login, password, Role.ADMIN);
        this.porcentajeCargoServicio = porcentajeCargoServicio == null ? BigDecimal.ZERO : porcentajeCargoServicio;
        this.cobroFijoEmision = cobroFijoEmision == null ? BigDecimal.ZERO : cobroFijoEmision;
    }

    public BigDecimal getPorcentajeCargoServicio() { return porcentajeCargoServicio; }
    public BigDecimal getCobroFijoEmision() { return cobroFijoEmision; }

    public void setPorcentajeCargoServicio(BigDecimal v) {
        this.porcentajeCargoServicio = v == null ? BigDecimal.ZERO : v;
    }

    public void setCobroFijoEmision(BigDecimal v) {
        this.cobroFijoEmision = v == null ? BigDecimal.ZERO : v;
    }
}


