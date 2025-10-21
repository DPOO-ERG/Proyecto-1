package co.edu.uniandes.proyecto1.domain.model.tiquete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class TiqueteComposite extends TiqueteAgrupacion {
    private final List<String> componentesIds; // ids de tiquetes incluidos

    protected TiqueteComposite(String id, String localidadId, boolean transferible,
                               java.math.BigDecimal precioAgrupacion, int restriccionTopeX) {
        super(id, localidadId, transferible, precioAgrupacion, restriccionTopeX);
        this.componentesIds = new ArrayList<>();
    }

    public List<String> getComponentesIds() {
        return Collections.unmodifiableList(componentesIds);
    }

    public void addComponente(String ticketId) {
        componentesIds.add(ticketId);
    }
}


