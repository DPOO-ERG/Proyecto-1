package co.edu.uniandes.proyecto1.domain.service;

import co.edu.uniandes.proyecto1.domain.model.oferta.Oferta;

import java.time.LocalDate;
import java.util.List;

public class OfferEngine {
    public double mejorDescuento(List<Oferta> ofertas, LocalDate fecha) {
        double best = 0.0;
        if (ofertas == null) return 0.0;
        for (Oferta o : ofertas) {
            if (o.activaEn(fecha)) {
                best = Math.max(best, o.getDescuentoPorcentual());
            }
        }
        return best;
    }
}


