package co.edu.uniandes.proyecto1.application.service;

import co.edu.uniandes.proyecto1.domain.model.localidad.Localidad;
import co.edu.uniandes.proyecto1.domain.model.tiquete.Tiquete;
import co.edu.uniandes.proyecto1.domain.repository.AsientoRepository;
import co.edu.uniandes.proyecto1.domain.repository.EventoRepository;
import co.edu.uniandes.proyecto1.domain.repository.LocalidadRepository;
import co.edu.uniandes.proyecto1.domain.repository.TiqueteRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ReportService {
    private final EventoRepository eventos;
    private final LocalidadRepository localidades;
    private final TiqueteRepository tiquetes;
    private final AsientoRepository asientos;

    public ReportService(EventoRepository eventos,
                         LocalidadRepository localidades,
                         TiqueteRepository tiquetes,
                         AsientoRepository asientos) {
        this.eventos = eventos;
        this.localidades = localidades;
        this.tiquetes = tiquetes;
        this.asientos = asientos;
    }

    public BigDecimal gananciasOrganizador(String organizadorId) {
        List<String> eventosIds = eventos.findByOrganizador(organizadorId).stream()
                .map(e -> e.getId()).collect(Collectors.toList());
        List<Localidad> locs = localidades.findAll().stream()
                .filter(l -> eventosIds.contains(l.getEventoId()))
                .collect(Collectors.toList());
        return sumarGanancias(locs);
    }

    public BigDecimal gananciasEvento(String eventoId) {
        List<Localidad> locs = localidades.findByEvento(eventoId);
        return sumarGanancias(locs);
    }

    public BigDecimal gananciasLocalidad(String localidadId) {
        Localidad l = localidades.findById(localidadId).orElse(null);
        if (l == null) return BigDecimal.ZERO;
        return sumarGanancias(java.util.List.of(l));
    }

    public double porcentajeVendidoEvento(String eventoId) {
        List<Localidad> locs = localidades.findByEvento(eventoId);
        long vendidos = 0;
        long disponibles = 0;
        for (Localidad l : locs) {
            vendidos += contarVendidosLocalidad(l.getId());
            disponibles += capacidadLocalidad(l);
        }
        if (disponibles == 0) return 0.0;
        return (vendidos * 1.0) / disponibles;
    }

    public double porcentajeVendidoLocalidad(String localidadId) {
        Localidad l = localidades.findById(localidadId).orElse(null);
        if (l == null) return 0.0;
        long vendidos = contarVendidosLocalidad(localidadId);
        long total = capacidadLocalidad(l);
        if (total == 0) return 0.0;
        return (vendidos * 1.0) / total;
    }

    private BigDecimal sumarGanancias(List<Localidad> locs) {
        BigDecimal total = BigDecimal.ZERO;
        for (Localidad l : locs) {
            for (Tiquete t : tiquetes.findByLocalidad(l.getId())) {
                if (t.isVendido()) {
                    BigDecimal neto = t.getPrecioFinal()
                            .subtract(t.getCargoServicio() == null ? BigDecimal.ZERO : t.getCargoServicio())
                            .subtract(t.getCuotaEmision() == null ? BigDecimal.ZERO : t.getCuotaEmision());
                    total = total.add(neto);
                }
            }
        }
        return total;
    }

    private long contarVendidosLocalidad(String localidadId) {
        return tiquetes.findByLocalidad(localidadId).stream()
                .filter(Tiquete::isVendido)
                .count();
    }

    private long capacidadLocalidad(Localidad l) {
        if (l.isEsNumerada()) {
            return asientos.findByLocalidad(l.getId()).size();
        } else {
            return l.getCapacidad();
        }
    }
}


