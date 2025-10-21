package co.edu.uniandes.proyecto1.infrastructure.csv.mappers;

import co.edu.uniandes.proyecto1.domain.model.user.*;
import co.edu.uniandes.proyecto1.infrastructure.csv.CsvFormat;

import java.math.BigDecimal;
import java.util.List;

public class UsuarioCsvMapper implements CsvMapper<Usuario> {
    @Override
    public String toLine(Usuario u) {
        String saldo = "";
        String porc = "";
        String fijo = "";
        if (u instanceof ClienteComprador cc) {
            saldo = cc.getSaldoVirtual().toPlainString();
        }
        if (u instanceof Administrador ad) {
            porc = ad.getPorcentajeCargoServicio().toPlainString();
            fijo = ad.getCobroFijoEmision().toPlainString();
        }
        return CsvFormat.join(
                u.getId(),
                u.getRole().name(),
                u.getLogin(),
                u.getPassword(),
                saldo,
                porc,
                fijo
        );
    }

    @Override
    public Usuario fromLine(String line) {
        List<String> c = CsvFormat.split(line);
        String id = c.get(0);
        Role role = Role.valueOf(c.get(1));
        String login = c.get(2);
        String password = c.get(3);
        String saldo = c.size() > 4 ? c.get(4) : "";
        String porc = c.size() > 5 ? c.get(5) : "";
        String fijo = c.size() > 6 ? c.get(6) : "";
        return switch (role) {
            case ADMIN -> new Administrador(id, login, password,
                    parseBig(porc), parseBig(fijo));
            case ORGANIZADOR -> new Organizador(id, login, password, parseBig(saldo));
            case CLIENTE -> new ClienteComprador(id, login, password, parseBig(saldo));
        };
    }

    private BigDecimal parseBig(String s) {
        if (s == null || s.isEmpty()) return BigDecimal.ZERO;
        return new BigDecimal(s);
    }
}


