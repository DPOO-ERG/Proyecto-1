package co.edu.uniandes.proyecto1.domain.model.user;

import java.math.BigDecimal;

public class Organizador extends ClienteComprador {
    public Organizador(String id, String login, String password, BigDecimal saldoVirtual) {
        super(id, login, password, saldoVirtual);
    }
}


