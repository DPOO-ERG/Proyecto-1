package co.edu.uniandes.proyecto1.application.service;

import co.edu.uniandes.proyecto1.domain.model.oferta.Oferta;
import co.edu.uniandes.proyecto1.domain.repository.OfertaRepository;

import java.util.List;

public class OfferService {
    private final OfertaRepository ofertas;

    public OfferService(OfertaRepository ofertas) {
        this.ofertas = ofertas;
    }

    public List<Oferta> listarPorLocalidad(String localidadId) {
        return ofertas.findByLocalidad(localidadId);
    }
}


