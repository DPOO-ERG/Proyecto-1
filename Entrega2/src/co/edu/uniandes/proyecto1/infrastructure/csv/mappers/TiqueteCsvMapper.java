package co.edu.uniandes.proyecto1.infrastructure.csv.mappers;

import co.edu.uniandes.proyecto1.domain.model.tiquete.*;
import co.edu.uniandes.proyecto1.infrastructure.csv.CsvFormat;

import java.math.BigDecimal;
import java.util.List;

public class TiqueteCsvMapper implements CsvMapper<Tiquete> {

    @Override
    public String toLine(Tiquete t) {
        String tipo;
        String numeroAsiento = "";
        String precioAgrupacion = "";
        String restriccionTopeX = "";
        if (t instanceof PaqueteDeluxe) {
            tipo = "PAQUETE_DELUXE";
        } else if (t instanceof TiqueteMultiple) {
            tipo = "MULTIPLE";
        } else if (t instanceof TiqueteAgrupacion agr) {
            tipo = "AGRUPACION";
            precioAgrupacion = agr.getPrecioAgrupacion().toPlainString();
            restriccionTopeX = Integer.toString(agr.getRestriccionTopeX());
        } else if (t instanceof TiqueteNumerado num) {
            tipo = "NUMERADO";
            numeroAsiento = num.getNumeroAsiento();
        } else {
            tipo = "BASICO";
        }

        return CsvFormat.join(
                t.getId(),
                tipo,
                t.getLocalidadId(),
                t.getCompradorId() == null ? "" : t.getCompradorId(),
                t.getPrecioFinal().toPlainString(),
                t.getCargoServicio().toPlainString(),
                t.getCuotaEmision().toPlainString(),
                Boolean.toString(t.isTransferible()),
                Boolean.toString(t.isVendido()),
                numeroAsiento,
                precioAgrupacion,
                restriccionTopeX
        );
    }

    @Override
    public Tiquete fromLine(String line) {
        List<String> c = CsvFormat.split(line);
        String id = c.get(0);
        String tipo = c.get(1);
        String localidadId = c.get(2);
        String compradorId = c.get(3);
        BigDecimal precioFinal = parseBig(c.get(4));
        BigDecimal cargoServicio = parseBig(c.get(5));
        BigDecimal cuotaEmision = parseBig(c.get(6));
        boolean transferible = Boolean.parseBoolean(c.get(7));
        boolean vendido = Boolean.parseBoolean(c.get(8));
        String numeroAsiento = c.size() > 9 ? c.get(9) : "";
        BigDecimal precioAgrupacion = (c.size() > 10 && !c.get(10).isEmpty()) ? new BigDecimal(c.get(10)) : null;
        int restriccionTopeX = (c.size() > 11 && !c.get(11).isEmpty()) ? Integer.parseInt(c.get(11)) : 0;

        Tiquete t;
        switch (tipo) {
            case "NUMERADO":
                t = new TiqueteNumerado(id, localidadId, transferible, numeroAsiento);
                break;
            case "AGRUPACION":
                t = new TiqueteAgrupacion(id, localidadId, transferible, precioAgrupacion, restriccionTopeX);
                break;
            case "MULTIPLE":
                t = new TiqueteMultiple(id, localidadId, transferible, precioAgrupacion, restriccionTopeX);
                break;
            case "PAQUETE_DELUXE":
                t = new PaqueteDeluxe(id, localidadId, transferible, precioAgrupacion, restriccionTopeX, "", false);
                break;
            case "BASICO":
            default:
                t = new TiqueteBasico(id, localidadId, transferible);
        }

        if (vendido && compradorId != null && !compradorId.isEmpty()) {
            // Marca los precios ya calculados
            t.marcarVendido(compradorId, precioFinal, cargoServicio, cuotaEmision);
        }
        return t;
    }

    private BigDecimal parseBig(String s) {
        if (s == null || s.isEmpty()) return BigDecimal.ZERO;
        return new BigDecimal(s);
    }
}


