package co.edu.uniandes.proyecto1.presentation.cli;

import co.edu.uniandes.proyecto1.application.service.AdminService;
import co.edu.uniandes.proyecto1.application.service.AppContext;
import co.edu.uniandes.proyecto1.application.service.AuthService;
import co.edu.uniandes.proyecto1.application.service.CatalogService;
import co.edu.uniandes.proyecto1.application.service.OrganizerService;
import co.edu.uniandes.proyecto1.application.service.TransferService;
import co.edu.uniandes.proyecto1.application.service.PurchaseService;
import co.edu.uniandes.proyecto1.application.service.DataSeeder;
import co.edu.uniandes.proyecto1.domain.exception.BusinessException;
import co.edu.uniandes.proyecto1.domain.model.user.Usuario;
import co.edu.uniandes.proyecto1.domain.model.user.ClienteComprador;
import co.edu.uniandes.proyecto1.domain.model.venue.Venue;
import co.edu.uniandes.proyecto1.domain.model.evento.Evento;
import co.edu.uniandes.proyecto1.domain.model.localidad.Localidad;
import co.edu.uniandes.proyecto1.domain.model.asiento.Asiento;
import co.edu.uniandes.proyecto1.domain.model.tiquete.Tiquete;
import co.edu.uniandes.proyecto1.domain.model.tiquete.TiqueteNumerado;
import co.edu.uniandes.proyecto1.domain.model.tiquete.TiqueteAgrupacion;
import co.edu.uniandes.proyecto1.domain.model.tiquete.PaqueteDeluxe;

import java.util.Scanner;
import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class AppCli {

    private final Scanner input;
    private final AppContext ctx;
    private final AuthService auth;
    private final AdminService adminService;
    private final OrganizerService organizerService;
    private final CatalogService catalogService;
    private final TransferService transferService;
    private final PurchaseService purchaseService;

    public AppCli() {
        this.input = new Scanner(System.in);
        this.ctx = new AppContext();
        this.auth = new AuthService(ctx.usuarios);
        this.adminService = new AdminService(ctx.usuarios, ctx.venues);
        this.organizerService = new OrganizerService(ctx.venues, ctx.eventos, ctx.localidades, ctx.asientos, ctx.ofertas);
        this.catalogService = new CatalogService(ctx.venues, ctx.eventos, ctx.localidades);
        this.transferService = new TransferService(ctx.tiquetes);
        this.purchaseService = new PurchaseService(ctx.usuarios, ctx.localidades, ctx.ofertas, ctx.asientos, ctx.tiquetes, ctx.offerEngine, ctx.pricingPolicy);

        // Seed mínimo de datos
        new DataSeeder(ctx.usuarios, ctx.venues, ctx.eventos, ctx.localidades, ctx.asientos, ctx.ofertas).seed();
    }

    public void run() {
        println("\n=== Boletería CLI (v0.1) ===");
        boolean running = true;
        while (running) {
            println("\n1) Registrarse (cliente)");
            println("2) Registrarse (organizador)");
            println("3) Iniciar sesión");
            println("4) Salir");
            print("Seleccione una opción: ");
            String option = input.nextLine().trim();
            try {
                switch (option) {
                    case "1":
                        registrarCliente();
                        break;
                    case "2":
                        registrarOrganizador();
                        break;
                    case "3":
                        Usuario u = login();
                        menuPorRol(u);
                        break;
                    case "4":
                        running = false;
                        break;
                    default:
                        println("Opción no válida, intente de nuevo.");
                }
            } catch (BusinessException ex) {
                println("Error: " + ex.getMessage());
            }
        }
        println("Saliendo. ¡Gracias!");
    }

    private void registrarCliente() {
        print("Login: ");
        String login = input.nextLine().trim();
        print("Password: ");
        String pass = input.nextLine().trim();
        auth.registrarCliente(login, pass);
        println("Cliente registrado.");
    }

    private void registrarOrganizador() {
        print("Login: ");
        String login = input.nextLine().trim();
        print("Password: ");
        String pass = input.nextLine().trim();
        auth.registrarOrganizador(login, pass);
        println("Organizador registrado.");
    }

    private Usuario login() {
        print("Login: ");
        String login = input.nextLine().trim();
        print("Password: ");
        String pass = input.nextLine().trim();
        Usuario u = auth.login(login, pass);
        println("Bienvenido, " + u.getLogin() + " (" + u.getRole() + ")");
        return u;
    }

    private void menuPorRol(Usuario u) {
        switch (u.getRole()) {
            case ADMIN:
                menuAdmin(u);
                break;
            case ORGANIZADOR:
                menuOrganizador(u);
                break;
            case CLIENTE:
                menuCliente(u);
                break;
        }
    }

    private void menuAdmin(Usuario admin) {
        boolean back = false;
        while (!back) {
            println("\n--- Menú ADMIN ---");
            println("1) Listar venues pendientes");
            println("2) Aprobar venue");
            println("3) Rechazar venue");
            println("4) Volver");
            print("Opción: ");
            String op = input.nextLine().trim();
            switch (op) {
                case "1":
                    ctx.venues.findByAprobado(false).forEach(v ->
                            println(v.getId() + " | " + v.getNombre() + " | cap=" + v.getCapacidadMaxima())
                    );
                    break;
                case "2":
                    print("ID venue: ");
                    adminService.aprobarVenue(input.nextLine().trim(), admin);
                    println("Aprobado.");
                    break;
                case "3":
                    print("ID venue: ");
                    adminService.rechazarVenue(input.nextLine().trim(), admin);
                    println("Rechazado.");
                    break;
                case "4":
                    back = true;
                    break;
                default:
                    println("Opción no válida.");
            }
        }
    }
    private void menuCliente(Usuario cliente) {
        boolean back = false;
        while (!back) {
            println("\n--- Menú CLIENTE ---");
            if (cliente instanceof ClienteComprador) {
                ClienteComprador cc = (ClienteComprador) cliente;
                println("Saldo actual: " + cc.getSaldoVirtual());
            }
            println("1) Ver catálogo (venues → eventos → localidades → asientos)");
            println("2) Comprar básico (localidad no numerada)");
            println("3) Comprar numerado (por asiento)");
            println("4) Comprar agrupación (cant > 1)");
            println("5) Crear paquete deluxe");
            println("6) Mis tiquetes");
            println("7) Transferir tiquete");
            println("8) Recargar saldo");
            println("9) Volver");
            print("Opción: ");
            String op = input.nextLine().trim();
            switch (op) {
                case "1":
                    explorarCatalogo();
                    break;
                case "2":
                    List<Venue> venuesB = catalogService.venuesAprobados();
                    Venue venueB = seleccionarDeListaPorNombreOIndice(venuesB,
                            v -> v.getNombre(), v -> v.getNombre(),
                            "Venues aprobados", "Nombre o # del venue (enter para volver): ");
                    if (venueB == null) break;
                    List<Evento> eventosB = ctx.eventos.findByVenue(venueB.getId());
                    Evento eventoB = seleccionarDeListaPorNombreOIndice(eventosB,
                            e -> e.getNombre(), e -> e.getNombre(),
                            "Eventos del venue", "Nombre o # del evento (enter para volver): ");
                    if (eventoB == null) break;
                    List<Localidad> locsB = ctx.localidades.findByEvento(eventoB.getId());
                    java.util.List<Localidad> locsNoNumB = new java.util.ArrayList<>();
                    for (Localidad l : locsB) { if (!l.isEsNumerada()) locsNoNumB.add(l); }
                    Localidad locB = seleccionarDeListaPorNombreOIndice(locsNoNumB,
                            l -> l.getNombre() + " | base=" + l.getPrecioBase(), l -> l.getNombre(),
                            "Localidades (no numeradas)", "Nombre o # de la localidad (enter para volver): ");
                    if (locB == null) break;
                    if (locB.isEsNumerada()) throw new BusinessException("Usa compra numerada para localidades numeradas");
                    print("Transferible? (true/false): "); boolean trb = Boolean.parseBoolean(input.nextLine());
                    var tb = purchaseService.comprarBasico(cliente, locB.getId(), trb);
                    println("Comprado tiquete: " + tb.getId());
                    break;
                case "3":
                    List<Venue> venuesN = catalogService.venuesAprobados();
                    Venue venueN = seleccionarDeListaPorNombreOIndice(venuesN,
                            v -> v.getNombre(), v -> v.getNombre(),
                            "Venues aprobados", "Nombre o # del venue (enter para volver): ");
                    if (venueN == null) break;
                    List<Evento> eventosN = ctx.eventos.findByVenue(venueN.getId());
                    Evento eventoN = seleccionarDeListaPorNombreOIndice(eventosN,
                            e -> e.getNombre(), e -> e.getNombre(),
                            "Eventos del venue", "Nombre o # del evento (enter para volver): ");
                    if (eventoN == null) break;
                    List<Localidad> locsN = ctx.localidades.findByEvento(eventoN.getId());
                    java.util.List<Localidad> locsNumN = new java.util.ArrayList<>();
                    for (Localidad l : locsN) { if (l.isEsNumerada()) locsNumN.add(l); }
                    Localidad locN = seleccionarDeListaPorNombreOIndice(locsNumN,
                            l -> l.getNombre() + " | base=" + l.getPrecioBase(), l -> l.getNombre(),
                            "Localidades (numeradas)", "Nombre o # de la localidad (enter para volver): ");
                    if (locN == null) break;
                    if (!locN.isEsNumerada()) throw new BusinessException("Localidad no es numerada");
                    List<Asiento> asientosDisp = new java.util.ArrayList<>();
                    for (Asiento a : ctx.asientos.findByLocalidad(locN.getId())) { if (a.isDisponible()) asientosDisp.add(a); }
                    Asiento asientoSel = seleccionarDeListaPorNombreOIndice(asientosDisp,
                            a -> a.getNumeroAsiento(), a -> a.getNumeroAsiento(),
                            "Asientos disponibles", "Número o # de asiento (enter para volver): ");
                    if (asientoSel == null) break;
                    print("Transferible? (true/false): "); boolean trn = Boolean.parseBoolean(input.nextLine());
                    var tn = purchaseService.comprarNumerado(cliente, asientoSel.getId(), trn);
                    println("Comprado tiquete numerado: " + tn.getId());
                    break;
                case "4":
                    List<Venue> venuesG = catalogService.venuesAprobados();
                    Venue venueG = seleccionarDeListaPorNombreOIndice(venuesG,
                            v -> v.getNombre(), v -> v.getNombre(),
                            "Venues aprobados", "Nombre o # del venue (enter para volver): ");
                    if (venueG == null) break;
                    List<Evento> eventosG = ctx.eventos.findByVenue(venueG.getId());
                    Evento eventoG = seleccionarDeListaPorNombreOIndice(eventosG,
                            e -> e.getNombre(), e -> e.getNombre(),
                            "Eventos del venue", "Nombre o # del evento (enter para volver): ");
                    if (eventoG == null) break;
                    List<Localidad> locsG = ctx.localidades.findByEvento(eventoG.getId());
                    java.util.List<Localidad> locsNoNumG = new java.util.ArrayList<>();
                    for (Localidad l : locsG) { if (!l.isEsNumerada()) locsNoNumG.add(l); }
                    Localidad locG = seleccionarDeListaPorNombreOIndice(locsNoNumG,
                            l -> l.getNombre() + " | base=" + l.getPrecioBase(), l -> l.getNombre(),
                            "Localidades (no numeradas)", "Nombre o # de la localidad (enter para volver): ");
                    if (locG == null) break;
                    if (locG.isEsNumerada()) throw new BusinessException("Usa compra numerada para localidades numeradas");
                    print("Cantidad: "); int cant = Integer.parseInt(input.nextLine());
                    print("Transferible? (true/false): "); boolean tra = Boolean.parseBoolean(input.nextLine());
                    var tg = purchaseService.comprarAgrupacion(cliente, locG.getId(), cant, tra);
                    println("Comprado agrupación: " + tg.getId());
                    break;
                case "5":
                    List<Venue> venuesD = catalogService.venuesAprobados();
                    Venue venueD = seleccionarDeListaPorNombreOIndice(venuesD,
                            v -> v.getNombre(), v -> v.getNombre(),
                            "Venues aprobados", "Nombre o # del venue (enter para volver): ");
                    if (venueD == null) break;
                    List<Evento> eventosD = ctx.eventos.findByVenue(venueD.getId());
                    Evento eventoD = seleccionarDeListaPorNombreOIndice(eventosD,
                            e -> e.getNombre(), e -> e.getNombre(),
                            "Eventos del venue", "Nombre o # del evento (enter para volver): ");
                    if (eventoD == null) break;
                    List<Localidad> locsD = ctx.localidades.findByEvento(eventoD.getId());
                    java.util.List<Localidad> locsNoNumD = new java.util.ArrayList<>();
                    for (Localidad l : locsD) { if (!l.isEsNumerada()) locsNoNumD.add(l); }
                    Localidad locD = seleccionarDeListaPorNombreOIndice(locsNoNumD,
                            l -> l.getNombre() + " | base=" + l.getPrecioBase(), l -> l.getNombre(),
                            "Localidades (no numeradas)", "Nombre o # de la localidad (enter para volver): ");
                    if (locD == null) break;
                    if (locD.isEsNumerada()) throw new BusinessException("Paquete deluxe simplificado solo con no numeradas");
                    print("Cantidad: "); int cantd = Integer.parseInt(input.nextLine());
                    print("Transferible? (true/false): "); boolean trd = Boolean.parseBoolean(input.nextLine());
                    print("Descripción beneficios: "); String desc = input.nextLine();
                    print("Incluye mercancía? (true/false): "); boolean merch = Boolean.parseBoolean(input.nextLine());
                    var pd = purchaseService.crearPaqueteDeluxe(cliente, locD.getId(), cantd, trd, desc, merch);
                    println("Creado paquete deluxe: " + pd.getId());
                    break;
                case "6":
                    imprimirTiquetesHumano(cliente);
                    break;
                case "7":
                    List<Tiquete> misT = ctx.tiquetes.findByComprador(cliente.getId());
                    Tiquete tsel = seleccionarDeListaPorNombreOIndice(misT,
                            t -> t.getId() + " | loc=" + t.getLocalidadId() + " | vendido=" + t.isVendido() + " | transferible=" + t.isTransferible(),
                            t -> t.getId(),
                            "Mis tiquetes", "Número o ID de tiquete (enter para volver): ");
                    if (tsel == null) break;
                    print("Login nuevo comprador: "); String loginNuevo = input.nextLine().trim();
                    var nuevoComprador = ctx.usuarios.findByLogin(loginNuevo).orElseThrow(() -> new BusinessException("Usuario no encontrado"));
                    transferService.transferir(cliente, tsel.getId(), nuevoComprador.getId());
                    println("Transferencia realizada.");
                    break;
                case "8":
                    recargarSaldo(cliente);
                    break;
                case "9":
                    back = true; break;
                default:
                    println("Opción no válida.");
            }
        }
    }

    private void explorarCatalogo() {
        println("\nVenues aprobados:");
        catalogService.venuesAprobados().forEach(v -> println(v.getId() + " | " + v.getNombre() + " | " + v.getUbicacion()));
        print("Ingrese Venue id (enter para volver): ");
        String venueId = input.nextLine().trim();
        if (venueId.isEmpty()) return;

        println("\nEventos del venue:");
        ctx.eventos.findByVenue(venueId).forEach(e -> println(e.getId() + " | " + e.getNombre() + " | fecha=" + e.getFecha()));
        print("Ingrese Evento id (enter para volver): ");
        String eventoId = input.nextLine().trim();
        if (eventoId.isEmpty()) return;

        println("\nLocalidades del evento:");
        ctx.localidades.findByEvento(eventoId).forEach(l -> println(l.getId() + " | " + l.getNombre() + " | base=" + l.getPrecioBase() + " | numerada=" + l.isEsNumerada()));

        print("¿Desea ver asientos de alguna localidad numerada? (id o enter para saltar): ");
        String localidadId = input.nextLine().trim();
        if (!localidadId.isEmpty()) {
            var loc = ctx.localidades.findById(localidadId).orElse(null);
            if (loc == null) { println("Localidad no existe"); return; }
            if (!loc.isEsNumerada()) { println("La localidad no es numerada"); return; }
            println("Asientos (disponibles) de la localidad:");
            ctx.asientos.findByLocalidad(localidadId).stream()
                    .filter(a -> a.isDisponible())
                    .forEach(a -> println(a.getId() + " | asiento=" + a.getNumeroAsiento()));
        }
    }

    private void menuOrganizador(Usuario organizador) {
        boolean back = false;
        while (!back) {
            println("\n--- Menú ORGANIZADOR ---");
            println("1) Proponer venue");
            println("2) Crear evento");
            println("3) Crear localidad");
            println("4) Cargar asientos");
            println("5) Crear oferta");
            println("6) Ver mis eventos");
            println("7) Volver");
            print("Opción: ");
            String op = input.nextLine().trim();
            switch (op) {
                case "1":
                    print("Nombre venue: "); String nv = input.nextLine();
                    print("Ubicación: "); String ub = input.nextLine();
                    print("Capacidad: "); int cap = Integer.parseInt(input.nextLine());
                    var v = organizerService.proponerVenue(organizador, nv, ub, cap);
                    println("Propuesto con id: " + v.getId());
                    break;
                case "2":
                    print("Venue id: "); String vid = input.nextLine();
                    print("Nombre evento: "); String ne = input.nextLine();
                    print("Fecha (YYYY-MM-DD): "); java.time.LocalDate f = java.time.LocalDate.parse(input.nextLine());
                    print("Tipo: "); String te = input.nextLine();
                    var e = organizerService.crearEvento(organizador, vid, ne, f, te);
                    println("Evento id: " + e.getId());
                    break;
                case "3":
                    print("Evento id: "); String eid = input.nextLine();
                    print("Nombre localidad: "); String nl = input.nextLine();
                    print("Precio base: "); java.math.BigDecimal pb = new java.math.BigDecimal(input.nextLine());
                    print("Es numerada? (true/false): "); boolean num = Boolean.parseBoolean(input.nextLine());
                    var loc = organizerService.crearLocalidad(organizador, eid, nl, pb, num);
                    println("Localidad id: " + loc.getId());
                    break;
                case "4":
                    print("Localidad numerada id: "); String lid = input.nextLine();
                    println("Ingresa asientos separados por coma (A1,A2,A3): ");
                    String[] arr = input.nextLine().split(",");
                    java.util.List<String> lista = new java.util.ArrayList<>();
                    for (String s : arr) { lista.add(s.trim()); }
                    organizerService.cargarAsientos(organizador, lid, lista);
                    println("Asientos cargados.");
                    break;
                case "5":
                    print("Localidad id: "); String olid = input.nextLine();
                    print("Descuento (0-1): "); double d = Double.parseDouble(input.nextLine());
                    print("Inicio (YYYY-MM-DD): "); var fi = java.time.LocalDate.parse(input.nextLine());
                    print("Fin (YYYY-MM-DD): "); var ff = java.time.LocalDate.parse(input.nextLine());
                    var of = organizerService.crearOferta(organizador, olid, d, fi, ff);
                    println("Oferta id: " + of.getId());
                    break;
                case "6":
                    ctx.eventos.findByOrganizador(organizador.getId()).forEach(ev ->
                            println(ev.getId() + " | " + ev.getNombre() + " | " + ev.getFecha())
                    );
                    break;
                case "7":
                    back = true; break;
                default:
                    println("Opción no válida.");
            }
        }
    }

    private void println(String s) {
        System.out.println(s);
    }

    private void print(String s) {
        System.out.print(s);
    }

    private <T> T seleccionarDeListaPorNombreOIndice(List<T> items,
                                                     java.util.function.Function<T, String> render,
                                                     java.util.function.Function<T, String> nombreClave,
                                                     String titulo,
                                                     String prompt) {
        if (items == null || items.isEmpty()) {
            println("No hay resultados.");
            return null;
        }
        println("\n" + titulo + ":");
        for (int i = 0; i < items.size(); i++) {
            println((i + 1) + ") " + render.apply(items.get(i)));
        }
        print(prompt);
        String sel = input.nextLine().trim();
        if (sel.isEmpty()) return null;
        try {
            int idx = Integer.parseInt(sel);
            if (idx >= 1 && idx <= items.size()) {
                return items.get(idx - 1);
            }
        } catch (NumberFormatException ignore) { }
        // por nombre/clave
        for (T it : items) {
            if (nombreClave.apply(it).equalsIgnoreCase(sel)) {
                return it;
            }
        }
        throw new BusinessException("Selección no válida");
    }

    private void recargarSaldo(Usuario cliente) {
        if (!(cliente instanceof ClienteComprador)) {
            println("Solo clientes pueden recargar saldo.");
            return;
        }
        ClienteComprador cc = (ClienteComprador) cliente;
        print("Monto a recargar: ");
        String s = input.nextLine().trim();
        if (s.isEmpty()) return;
        BigDecimal monto;
        try {
            monto = new BigDecimal(s);
        } catch (NumberFormatException ex) {
            throw new BusinessException("Monto inválido");
        }
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Monto debe ser > 0");
        }
        cc.setSaldoVirtual(cc.getSaldoVirtual().add(monto));
        ctx.usuarios.update(cc);
        println("Saldo actualizado: " + cc.getSaldoVirtual());
    }

    private void imprimirTiquetesHumano(Usuario cliente) {
        List<Tiquete> lista = ctx.tiquetes.findByComprador(cliente.getId());
        if (lista.isEmpty()) {
            println("No tienes tiquetes aún.");
            return;
        }
        println("\nMis tiquetes:");
        for (Tiquete t : lista) {
            Localidad loc = ctx.localidades.findById(t.getLocalidadId()).orElse(null);
            String locNombre = loc == null ? t.getLocalidadId() : loc.getNombre();
            String eventoNombre = "";
            String fechaEvento = "";
            if (loc != null) {
                Evento ev = ctx.eventos.findById(loc.getEventoId()).orElse(null);
                if (ev != null) {
                    eventoNombre = ev.getNombre();
                    fechaEvento = ev.getFecha() == null ? "" : ev.getFecha().toString();
                }
            }
            String tipo = "Básico";
            String extra = "";
            if (t instanceof TiqueteNumerado tn) {
                tipo = "Numerado";
                String estadoAsiento = "";
                if (loc != null) {
                    Asiento a = ctx.asientos.findByLocalidadAndNumero(loc.getId(), tn.getNumeroAsiento()).orElse(null);
                    if (a != null) {
                        estadoAsiento = a.isDisponible() ? "disponible" : "reservado";
                    }
                }
                extra = " | asiento=" + tn.getNumeroAsiento() + (estadoAsiento.isEmpty() ? "" : " (" + estadoAsiento + ")");
            } else if (t instanceof PaqueteDeluxe) {
                tipo = "Paquete Deluxe";
            } else if (t instanceof TiqueteAgrupacion ta) {
                tipo = "Agrupación x" + ta.getRestriccionTopeX();
            }
            String precio = t.getPrecioFinal() == null ? "0.00" : t.getPrecioFinal().setScale(2, RoundingMode.HALF_UP).toPlainString();
            println(t.getId() + " | " + tipo + " | evento=" + eventoNombre + (fechaEvento.isEmpty()? "" : " (" + fechaEvento + ")") + " | localidad=" + locNombre + extra
                    + " | total=$" + precio + " | transferible=" + t.isTransferible());
        }
    }
}


