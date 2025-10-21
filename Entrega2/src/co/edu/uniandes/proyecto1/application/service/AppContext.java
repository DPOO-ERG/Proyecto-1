package co.edu.uniandes.proyecto1.application.service;

import co.edu.uniandes.proyecto1.domain.repository.*;
import co.edu.uniandes.proyecto1.domain.service.DefaultPricingPolicy;
import co.edu.uniandes.proyecto1.domain.service.OfferEngine;
import co.edu.uniandes.proyecto1.domain.service.PricingPolicy;
import co.edu.uniandes.proyecto1.infrastructure.csv.repository.*;

public class AppContext {
    public final UsuarioRepository usuarios = new CsvUsuarioRepository();
    public final VenueRepository venues = new CsvVenueRepository();
    public final EventoRepository eventos = new CsvEventoRepository();
    public final LocalidadRepository localidades = new CsvLocalidadRepository();
    public final OfertaRepository ofertas = new CsvOfertaRepository();
    public final AsientoRepository asientos = new CsvAsientoRepository();
    public final TiqueteRepository tiquetes = new CsvTiqueteRepository();
    public final TiqueteAgregadoRepository tiquetesAgregados = new CsvTiqueteAgregadoRepository();

    public final PricingPolicy pricingPolicy = new DefaultPricingPolicy();
    public final OfferEngine offerEngine = new OfferEngine();
}


