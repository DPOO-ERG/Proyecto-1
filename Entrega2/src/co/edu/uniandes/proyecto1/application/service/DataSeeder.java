package co.edu.uniandes.proyecto1.application.service;

import co.edu.uniandes.proyecto1.domain.model.asiento.Asiento;
import co.edu.uniandes.proyecto1.domain.model.evento.Evento;
import co.edu.uniandes.proyecto1.domain.model.localidad.Localidad;
import co.edu.uniandes.proyecto1.domain.model.oferta.Oferta;
import co.edu.uniandes.proyecto1.domain.model.user.ClienteComprador;
import co.edu.uniandes.proyecto1.domain.model.user.Administrador;
import co.edu.uniandes.proyecto1.domain.model.user.Organizador;
import co.edu.uniandes.proyecto1.domain.model.user.Usuario;
import co.edu.uniandes.proyecto1.domain.model.venue.Venue;
import co.edu.uniandes.proyecto1.domain.repository.*;
import co.edu.uniandes.proyecto1.domain.service.IdGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

public class DataSeeder {
    private final UsuarioRepository usuarios;
    private final VenueRepository venues;
    private final EventoRepository eventos;
    private final LocalidadRepository localidades;
    private final AsientoRepository asientos;
    private final OfertaRepository ofertas;

    public DataSeeder(UsuarioRepository usuarios, VenueRepository venues, EventoRepository eventos,
                      LocalidadRepository localidades, AsientoRepository asientos, OfertaRepository ofertas) {
        this.usuarios = usuarios;
        this.venues = venues;
        this.eventos = eventos;
        this.localidades = localidades;
        this.asientos = asientos;
        this.ofertas = ofertas;
    }

    public void seed() {
        // Admin por defecto (visible en CSV de raíz)
        if (usuarios.findByLogin("admin").isEmpty()) {
            Administrador admin = new Administrador(IdGenerator.newId(), "admin", "admin",
                    new BigDecimal("0.10"), new BigDecimal("1.00"));
            usuarios.save(admin);
        }

        // Usuario cliente demo
        String cliId = usuarios.findByLogin("cli")
                .map(Usuario::getId)
                .orElseGet(() -> {
                    ClienteComprador cli = new ClienteComprador(IdGenerator.newId(), "cli", "cli", new BigDecimal("500.00"));
                    usuarios.save(cli);
                    return cli.getId();
                });

        // Organizador demo
        String orgId = usuarios.findByLogin("org")
                .map(Usuario::getId)
                .orElseGet(() -> {
                    Organizador org = new Organizador(IdGenerator.newId(), "org", "org", BigDecimal.ZERO);
                    usuarios.save(org);
                    return org.getId();
                });

        // Venue aprobado
        Venue v = venues.findByNombre("Arena Central")
                .orElseGet(() -> venues.save(new Venue(IdGenerator.newId(), "Arena Central", "Ciudad", 100, true, orgId)));

        // Evento de prueba
        Evento e = eventos.findByVenueAndNombre(v.getId(), "Concierto Demo")
                .orElseGet(() -> eventos.save(new Evento(IdGenerator.newId(), "Concierto Demo", LocalDate.now().plusDays(14), "MUSICA", v.getId(), orgId)));

        // Localidades
        Localidad lGeneral = localidades.findByEventoAndNombre(e.getId(), "General")
                .orElseGet(() -> localidades.save(new Localidad(IdGenerator.newId(), e.getId(), "General", new BigDecimal("50.00"), false)));

        Localidad lPlatea = localidades.findByEventoAndNombre(e.getId(), "Platea")
                .orElseGet(() -> localidades.save(new Localidad(IdGenerator.newId(), e.getId(), "Platea", new BigDecimal("80.00"), true)));

        // Asientos para Platea (si faltan)
        Arrays.asList("A1", "A2", "B1", "B2").forEach(num -> {
            if (asientos.findByLocalidadAndNumero(lPlatea.getId(), num).isEmpty()) {
                Asiento a = new Asiento(IdGenerator.newId(), lPlatea.getId(), num, true);
                asientos.save(a);
            }
        });

        // Oferta para General (si no hay ya ofertas)
        if (ofertas.findByLocalidad(lGeneral.getId()).isEmpty()) {
            Oferta of = new Oferta(IdGenerator.newId(), lGeneral.getId(), 0.20,
                    LocalDate.now().minusDays(5), LocalDate.now().plusDays(20));
            ofertas.save(of);
        }
    }
}


