package co.edu.uniandes.proyecto1.application.service;

import co.edu.uniandes.proyecto1.domain.model.evento.Evento;
import co.edu.uniandes.proyecto1.domain.model.localidad.Localidad;
import co.edu.uniandes.proyecto1.domain.model.venue.Venue;
import co.edu.uniandes.proyecto1.domain.repository.EventoRepository;
import co.edu.uniandes.proyecto1.domain.repository.LocalidadRepository;
import co.edu.uniandes.proyecto1.domain.repository.VenueRepository;

import java.util.List;

public class CatalogService {
    private final VenueRepository venues;
    private final EventoRepository eventos;
    private final LocalidadRepository localidades;

    public CatalogService(VenueRepository venues, EventoRepository eventos, LocalidadRepository localidades) {
        this.venues = venues;
        this.eventos = eventos;
        this.localidades = localidades;
    }

    public List<Venue> venuesAprobados() { return venues.findByAprobado(true); }
    public List<Evento> eventosPorVenue(String venueId) { return eventos.findByVenue(venueId); }
    public List<Localidad> localidadesPorEvento(String eventoId) { return localidades.findByEvento(eventoId); }
}


