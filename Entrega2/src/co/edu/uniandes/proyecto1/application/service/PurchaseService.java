package co.edu.uniandes.proyecto1.application.service;

import co.edu.uniandes.proyecto1.domain.exception.BusinessException;
import co.edu.uniandes.proyecto1.domain.model.asiento.Asiento;
import co.edu.uniandes.proyecto1.domain.model.localidad.Localidad;
import co.edu.uniandes.proyecto1.domain.model.tiquete.Tiquete;
import co.edu.uniandes.proyecto1.domain.model.tiquete.TiqueteAgrupacion;
import co.edu.uniandes.proyecto1.domain.model.tiquete.TiqueteBasico;
import co.edu.uniandes.proyecto1.domain.model.tiquete.TiqueteNumerado;
import co.edu.uniandes.proyecto1.domain.model.tiquete.PaqueteDeluxe;
import co.edu.uniandes.proyecto1.domain.model.user.Administrador;
import co.edu.uniandes.proyecto1.domain.model.user.ClienteComprador;
import co.edu.uniandes.proyecto1.domain.model.user.Role;
import co.edu.uniandes.proyecto1.domain.model.user.Usuario;
import co.edu.uniandes.proyecto1.domain.repository.*;
import co.edu.uniandes.proyecto1.domain.service.OfferEngine;
import co.edu.uniandes.proyecto1.domain.service.PriceBreakdown;
import co.edu.uniandes.proyecto1.domain.service.PricingPolicy;
import co.edu.uniandes.proyecto1.domain.service.TicketFactory;
import co.edu.uniandes.proyecto1.domain.model.evento.Evento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class PurchaseService {
    private final UsuarioRepository usuarios;
    private final EventoRepository eventos;
    private final LocalidadRepository localidades;
    private final OfertaRepository ofertas;
    private final AsientoRepository asientos;
    private final TiqueteRepository tiquetes;
    private final OfferEngine offerEngine;
    private final PricingPolicy pricingPolicy;

    public PurchaseService(UsuarioRepository usuarios,
                           EventoRepository eventos,
                           LocalidadRepository localidades,
                           OfertaRepository ofertas,
                           AsientoRepository asientos,
                           TiqueteRepository tiquetes,
                           OfferEngine offerEngine,
                           PricingPolicy pricingPolicy) {
        this.usuarios = usuarios;
        this.eventos = eventos;
        this.localidades = localidades;
        this.ofertas = ofertas;
        this.asientos = asientos;
        this.tiquetes = tiquetes;
        this.offerEngine = offerEngine;
        this.pricingPolicy = pricingPolicy;
    }

    public TiqueteBasico comprarBasico(Usuario comprador, String localidadId, boolean transferible) {
        Localidad l = localidades.findById(localidadId).orElseThrow(() -> new BusinessException("Localidad no existe"));
        if (l.isEsNumerada()) throw new BusinessException("Usa compra numerada para localidades numeradas");
        // Bloqueo si evento cancelado
        var ev = eventos.findById(l.getEventoId()).orElse(null);
        if (ev != null && ev.getEstado() == co.edu.uniandes.proyecto1.domain.model.evento.EstadoEvento.CANCELADO) {
            throw new BusinessException("Evento cancelado");
        }
        // Validación de capacidad para no numeradas
        long vendidos = tiquetes.findByLocalidad(localidadId).stream().filter(co.edu.uniandes.proyecto1.domain.model.tiquete.Tiquete::isVendido).count();
        if (vendidos + 1 > l.getCapacidad()) {
            throw new BusinessException("Capacidad agotada para la localidad");
        }
        boolean cortesia = esCortesia(comprador, l);
        PriceBreakdown pb = null;
        if (!cortesia) {
            pb = calcularPrecioLocalidad(l);
            debitarSaldo(comprador, pb.getTotal());
        }
        TiqueteBasico t = TicketFactory.crearBasico(localidadId, transferible);
        if (cortesia) {
            t.marcarVendido(comprador.getId(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        } else {
            t.marcarVendido(comprador.getId(), pb.getTotal(), pb.getCargoServicio(), pb.getCuotaEmision());
        }
        return (TiqueteBasico) tiquetes.save(t);
    }

    public TiqueteNumerado comprarNumerado(Usuario comprador, String asientoId, boolean transferible) {
        Asiento a = asientos.findById(asientoId).orElseThrow(() -> new BusinessException("Asiento no existe"));
        if (!a.isDisponible()) throw new BusinessException("Asiento no disponible");
        Localidad l = localidades.findById(a.getLocalidadId()).orElseThrow(() -> new BusinessException("Localidad no existe"));
        if (!l.isEsNumerada()) throw new BusinessException("Localidad no es numerada");
        // Bloqueo si evento cancelado
        var ev = eventos.findById(l.getEventoId()).orElse(null);
        if (ev != null && ev.getEstado() == co.edu.uniandes.proyecto1.domain.model.evento.EstadoEvento.CANCELADO) {
            throw new BusinessException("Evento cancelado");
        }
        boolean cortesia = esCortesia(comprador, l);
        PriceBreakdown pb = null;
        if (!cortesia) {
            pb = calcularPrecioLocalidad(l);
            debitarSaldo(comprador, pb.getTotal());
        }
        TiqueteNumerado t = TicketFactory.crearNumerado(l.getId(), transferible, a.getNumeroAsiento());
        if (cortesia) {
            t.marcarVendido(comprador.getId(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        } else {
            t.marcarVendido(comprador.getId(), pb.getTotal(), pb.getCargoServicio(), pb.getCuotaEmision());
        }
        a.setDisponible(false);
        asientos.update(a);
        return (TiqueteNumerado) tiquetes.save(t);
    }

    public TiqueteAgrupacion comprarAgrupacion(Usuario comprador, String localidadId, int cantidad, boolean transferible) {
        if (cantidad <= 0) throw new BusinessException("Cantidad debe ser > 0");
        Localidad l = localidades.findById(localidadId).orElseThrow(() -> new BusinessException("Localidad no existe"));
        if (l.isEsNumerada()) throw new BusinessException("Usa compra numerada para localidades numeradas");
        // Bloqueo si evento cancelado
        var ev = eventos.findById(l.getEventoId()).orElse(null);
        if (ev != null && ev.getEstado() == co.edu.uniandes.proyecto1.domain.model.evento.EstadoEvento.CANCELADO) {
            throw new BusinessException("Evento cancelado");
        }
        // Validación de capacidad para no numeradas
        long vendidos = tiquetes.findByLocalidad(localidadId).stream().filter(co.edu.uniandes.proyecto1.domain.model.tiquete.Tiquete::isVendido).count();
        if (vendidos + cantidad > l.getCapacidad()) {
            throw new BusinessException("Capacidad insuficiente para la cantidad solicitada");
        }
        // Precio base del grupo = precioBase * cantidad
        BigDecimal baseGrupo = l.getPrecioBase().multiply(BigDecimal.valueOf(cantidad));
        boolean cortesia = esCortesia(comprador, l);
        PriceBreakdown pb = null;
        if (!cortesia) {
            double best = offerEngine.mejorDescuento(ofertas.findByLocalidad(l.getId()), LocalDate.now());
            Administrador admin = usuarios.findAll().stream()
                    .filter(u -> u.getRole() == Role.ADMIN)
                    .map(u -> (Administrador) u)
                    .min(Comparator.comparing(Administrador::getPorcentajeCargoServicio))
                    .orElse(new Administrador("admin-default", "admin", "admin", BigDecimal.ZERO, BigDecimal.ZERO));
            pb = pricingPolicy.calcularPrecio(baseGrupo, best, admin.getPorcentajeCargoServicio(), admin.getCobroFijoEmision());
            debitarSaldo(comprador, pb.getTotal());
        }
        TiqueteAgrupacion t = TicketFactory.crearAgrupacion(localidadId, transferible, baseGrupo, cantidad);
        if (cortesia) {
            t.marcarVendido(comprador.getId(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        } else {
            t.marcarVendido(comprador.getId(), pb.getTotal(), pb.getCargoServicio(), pb.getCuotaEmision());
        }
        return (TiqueteAgrupacion) tiquetes.save(t);
    }

    public PaqueteDeluxe crearPaqueteDeluxe(Usuario comprador, String localidadId, int cantidad, boolean transferible,
                                            String descripcionBeneficios, boolean incluyeMercancia) {
        if (cantidad <= 0) throw new BusinessException("Cantidad debe ser > 0");
        Localidad l = localidades.findById(localidadId).orElseThrow(() -> new BusinessException("Localidad no existe"));
        if (l.isEsNumerada()) throw new BusinessException("Paquete deluxe simplificado solo con no numeradas");
        // Bloqueo si evento cancelado
        var ev = eventos.findById(l.getEventoId()).orElse(null);
        if (ev != null && ev.getEstado() == co.edu.uniandes.proyecto1.domain.model.evento.EstadoEvento.CANCELADO) {
            throw new BusinessException("Evento cancelado");
        }
        BigDecimal baseGrupo = l.getPrecioBase().multiply(BigDecimal.valueOf(cantidad));
        double best = offerEngine.mejorDescuento(ofertas.findByLocalidad(l.getId()), LocalDate.now());
        Administrador admin = usuarios.findAll().stream()
                .filter(u -> u.getRole() == Role.ADMIN)
                .map(u -> (Administrador) u)
                .min(Comparator.comparing(Administrador::getPorcentajeCargoServicio))
                .orElse(new Administrador("admin-default", "admin", "admin", BigDecimal.ZERO, BigDecimal.ZERO));
        PriceBreakdown pb = pricingPolicy.calcularPrecio(baseGrupo, best, admin.getPorcentajeCargoServicio(), admin.getCobroFijoEmision());
        boolean cortesia = esCortesia(comprador, l);
        if (!cortesia) {
            debitarSaldo(comprador, pb.getTotal());
        }
        PaqueteDeluxe p = TicketFactory.crearPaqueteDeluxe(localidadId, transferible, baseGrupo, cantidad, descripcionBeneficios, incluyeMercancia);
        if (cortesia) {
            p.marcarVendido(comprador.getId(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        } else {
            p.marcarVendido(comprador.getId(), pb.getTotal(), pb.getCargoServicio(), pb.getCuotaEmision());
        }
        return (PaqueteDeluxe) tiquetes.save(p);
    }

    private PriceBreakdown calcularPrecioLocalidad(Localidad l) {
        double best = offerEngine.mejorDescuento(ofertas.findByLocalidad(l.getId()), LocalDate.now());
        Administrador admin = usuarios.findAll().stream()
                .filter(u -> u.getRole() == Role.ADMIN)
                .map(u -> (Administrador) u)
                .min(Comparator.comparing(Administrador::getPorcentajeCargoServicio))
                .orElse(new Administrador("admin-default", "admin", "admin", BigDecimal.ZERO, BigDecimal.ZERO));
        return pricingPolicy.calcularPrecio(l.getPrecioBase(), best,
                admin.getPorcentajeCargoServicio(), admin.getCobroFijoEmision());
    }

    private void debitarSaldo(Usuario u, BigDecimal total) {
        if (u instanceof ClienteComprador c) {
            if (c.getSaldoVirtual().compareTo(total) < 0) {
                throw new BusinessException("Saldo insuficiente");
            }
            c.setSaldoVirtual(c.getSaldoVirtual().subtract(total));
            usuarios.update(c);
        }
    }

    private boolean esCortesia(Usuario comprador, Localidad localidad) {
        if (comprador == null || localidad == null) return false;
        var ev = eventos.findById(localidad.getEventoId()).orElse(null);
        if (ev == null) return false;
        return comprador.getId().equals(ev.getOrganizadorId());
    }
}


