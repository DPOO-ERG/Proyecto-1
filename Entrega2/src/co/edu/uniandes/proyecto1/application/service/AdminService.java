package co.edu.uniandes.proyecto1.application.service;

import co.edu.uniandes.proyecto1.domain.exception.BusinessException;
import co.edu.uniandes.proyecto1.domain.model.user.Administrador;
import co.edu.uniandes.proyecto1.domain.model.user.Role;
import co.edu.uniandes.proyecto1.domain.model.user.Usuario;
import co.edu.uniandes.proyecto1.domain.model.venue.Venue;
import co.edu.uniandes.proyecto1.domain.repository.UsuarioRepository;
import co.edu.uniandes.proyecto1.domain.repository.VenueRepository;
import co.edu.uniandes.proyecto1.domain.repository.EventoRepository;
import co.edu.uniandes.proyecto1.domain.model.evento.Evento;
import co.edu.uniandes.proyecto1.domain.model.evento.EstadoEvento;

import java.math.BigDecimal;
import java.util.List;

public class AdminService {
    private final UsuarioRepository usuarios;
    private final VenueRepository venues;
    private final EventoRepository eventos;

    public AdminService(UsuarioRepository usuarios, VenueRepository venues, EventoRepository eventos) {
        this.usuarios = usuarios;
        this.venues = venues;
        this.eventos = eventos;
    }

    public List<Venue> listarPendientes() {
        return venues.findByAprobado(false);
    }

    public void aprobarVenue(String venueId, Usuario admin) {
        checkAdmin(admin);
        Venue v = venues.findById(venueId).orElseThrow(() -> new BusinessException("Venue no encontrado"));
        v.setAprobado(true);
        venues.update(v);
    }

    public void rechazarVenue(String venueId, Usuario admin) {
        checkAdmin(admin);
        Venue v = venues.findById(venueId).orElseThrow(() -> new BusinessException("Venue no encontrado"));
        v.setAprobado(false);
        venues.update(v);
    }

    public void actualizarCargos(Usuario admin, BigDecimal porcentaje, BigDecimal cuotaFija) {
        checkAdmin(admin);
        Administrador a = (Administrador) admin;
        a.setPorcentajeCargoServicio(porcentaje);
        a.setCobroFijoEmision(cuotaFija);
        usuarios.update(a);
    }

    private void checkAdmin(Usuario u) {
        if (u == null || u.getRole() != Role.ADMIN) {
            throw new BusinessException("Operación solo para ADMIN");
        }
    }

    public void aprobarCancelacion(String eventoId, Usuario admin) {
        checkAdmin(admin);
        Evento e = eventos.findById(eventoId).orElseThrow(() -> new BusinessException("Evento no encontrado"));
        e.setEstado(EstadoEvento.CANCELADO);
        eventos.update(e);
    }

    public void rechazarCancelacion(String eventoId, Usuario admin) {
        checkAdmin(admin);
        Evento e = eventos.findById(eventoId).orElseThrow(() -> new BusinessException("Evento no encontrado"));
        e.setEstado(EstadoEvento.ACTIVO);
        eventos.update(e);
    }
}


