package co.edu.uniandes.proyecto1.domain.model.user;

import java.math.BigDecimal;

public class ClienteComprador extends Usuario {
    private BigDecimal saldoVirtual;

    public ClienteComprador(String id, String login, String password, BigDecimal saldoVirtual) {
        super(id, login, password, Role.CLIENTE);
        this.saldoVirtual = saldoVirtual == null ? BigDecimal.ZERO : saldoVirtual;
    }

    // Constructor protegido para subclases con rol específico (p.ej., Organizador)
    protected ClienteComprador(String id, String login, String password, BigDecimal saldoVirtual, Role role) {
        super(id, login, password, role);
        this.saldoVirtual = saldoVirtual == null ? BigDecimal.ZERO : saldoVirtual;
    }

    public BigDecimal getSaldoVirtual() { return saldoVirtual; }

    public void setSaldoVirtual(BigDecimal nuevoSaldo) {
        this.saldoVirtual = nuevoSaldo == null ? BigDecimal.ZERO : nuevoSaldo;
    }
}


