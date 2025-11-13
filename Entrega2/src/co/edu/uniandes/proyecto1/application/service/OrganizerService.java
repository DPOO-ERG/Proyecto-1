package co.edu.uniandes.proyecto1.application.service;

import co.edu.uniandes.proyecto1.domain.exception.BusinessException;
import co.edu.uniandes.proyecto1.domain.model.asiento.Asiento;
import co.edu.uniandes.proyecto1.domain.model.evento.Evento;
import co.edu.uniandes.proyecto1.domain.model.localidad.Localidad;
import co.edu.uniandes.proyecto1.domain.model.oferta.Oferta;
import co.edu.uniandes.proyecto1.domain.model.user.Role;
import co.edu.uniandes.proyecto1.domain.model.user.Usuario;
import co.edu.uniandes.proyecto1.domain.model.venue.Venue;
import co.edu.uniandes.proyecto1.domain.repository.*;
import co.edu.uniandes.proyecto1.domain.service.IdGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrganizerService {
    private final VenueRepository venues;
    private final EventoRepository eventos;
    private final LocalidadRepository localidades;
    private final AsientoRepository asientos;
    private final OfertaRepository ofertas;

    public OrganizerService(VenueRepository venues, EventoRepository eventos, LocalidadRepository localidades,
                            AsientoRepository asientos, OfertaRepository ofertas) {
        this.venues = venues;
        this.eventos = eventos;
        this.localidades = localidades;
        this.asientos = asientos;
        this.ofertas = ofertas;
    }

    private void checkOrganizador(Usuario u) {
        if (u == null || u.getRole() != Role.ORGANIZADOR) {
            throw new BusinessException("Operación solo para ORGANIZADOR");
        }
    }

    public Venue proponerVenue(Usuario organizador, String nombre, String ubicacion, int capacidad) {
        checkOrganizador(organizador);
        Venue v = new Venue(IdGenerator.newId(), nombre, ubicacion, capacidad, false, organizador.getId());
        return venues.save(v);
    }

    public Evento crearEvento(Usuario organizador, String venueId, String nombre, LocalDate fecha, String tipo) {
        checkOrganizador(organizador);
        Venue v = venues.findById(venueId).orElseThrow(() -> new BusinessException("Venue no existe"));
        if (!v.isAprobado()) throw new BusinessException("Venue no aprobado");
        Evento e = new Evento(IdGenerator.newId(), nombre, fecha, tipo, venueId, organizador.getId(),
                co.edu.uniandes.proyecto1.domain.model.evento.EstadoEvento.ACTIVO);
        return eventos.save(e);
    }

    public Localidad crearLocalidad(Usuario organizador, String eventoId, String nombre, BigDecimal precioBase, boolean esNumerada) {
        // Compatibilidad: capacidad por defecto 0 si no se especifica
        return crearLocalidad(organizador, eventoId, nombre, precioBase, esNumerada, 0);
    }

    public Localidad crearLocalidad(Usuario organizador, String eventoId, String nombre, BigDecimal precioBase, boolean esNumerada, int capacidad) {
        checkOrganizador(organizador);
        Evento e = eventos.findById(eventoId).orElseThrow(() -> new BusinessException("Evento no existe"));
        if (!e.getOrganizadorId().equals(organizador.getId())) throw new BusinessException("No es tu evento");
        Localidad l = new Localidad(IdGenerator.newId(), eventoId, nombre, precioBase, esNumerada, capacidad);
        return localidades.save(l);
    }

    public void cargarAsientos(Usuario organizador, String localidadId, List<String> numerosAsientos) {
        checkOrganizador(organizador);
        Localidad l = localidades.findById(localidadId).orElseThrow(() -> new BusinessException("Localidad no existe"));
        if (!l.isEsNumerada()) throw new BusinessException("Localidad no es numerada");
        for (String num : numerosAsientos) {
            Asiento a = new Asiento(IdGenerator.newId(), localidadId, num, true);
            asientos.save(a);
        }
    }

    public Oferta crearOferta(Usuario organizador, String localidadId, double descuento, LocalDate inicio, LocalDate fin) {
        checkOrganizador(organizador);
        Localidad l = localidades.findById(localidadId).orElseThrow(() -> new BusinessException("Localidad no existe"));
        Oferta o = new Oferta(IdGenerator.newId(), localidadId, descuento, inicio, fin);
        return ofertas.save(o);
    }

    public void solicitarCancelacionEvento(Usuario organizador, String eventoId) {
        checkOrganizador(organizador);
        Evento e = eventos.findById(eventoId).orElseThrow(() -> new BusinessException("Evento no existe"));
        if (!e.getOrganizadorId().equals(organizador.getId())) {
            throw new BusinessException("No es tu evento");
        }
        if (e.getEstado() == co.edu.uniandes.proyecto1.domain.model.evento.EstadoEvento.CANCELADO) {
            throw new BusinessException("El evento ya está cancelado");
        }
        e.setEstado(co.edu.uniandes.proyecto1.domain.model.evento.EstadoEvento.CANCELACION_SOLICITADA);
        eventos.update(e);
    }
}


