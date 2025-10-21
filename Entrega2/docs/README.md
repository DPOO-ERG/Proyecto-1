## Documentación de diseño (Mermaid + guía)

Este directorio contiene diagramas Mermaid y una guía breve para comprender la arquitectura, el modelo de dominio y los flujos críticos del sistema de boletería.

### Índice
- Clases (detallado): `clases-detallado.mmd`
- Clases (alto nivel): `clases-alto-nivel.mmd`
- Componentes/capas: `arquitectura-componentes.mmd`
- Paquetes: `paquetes.mmd`
- Despliegue: `despliegue.mmd`
- Estados del tiquete: `estados-tiquete.mmd`
- Actividad de compra: `actividad-compra.mmd`
- Secuencias:
  - Autenticación: `seq-autenticacion.mmd`
  - Catálogo/Búsqueda: `seq-catalogo-busqueda.mmd`
  - Compra: `seq-compra.mmd`
  - Transferencia: `seq-transferencia.mmd`
  - Admin aprueba venue: `seq-admin-aprobar-venue.mmd`
  - Organizador crea evento: `seq-organizador-crear-evento.mmd`
  - Organizador carga asientos: `seq-organizador-cargar-asientos.mmd`
  - Casos de uso: `casos-uso.mmd`

### Arquitectura (capas y justificación)
- **Estilo**: Arquitectura en capas (Layered Architecture) con separación estricta de responsabilidades.
- **Presentación**: `AppCli` es la interfaz de usuario. Solo I/O y delegación a servicios de aplicación.
- **Aplicación**: servicios orquestadores (`AuthService`, `AdminService`, `OrganizerService`, `CatalogService`, `PurchaseService`, `TransferService`, `OfferService`) coordinan transacciones y reglas de caso de uso; no contienen lógica de persistencia ni detalles de UI.
- **Dominio**: entidades y comportamiento del negocio:
  - Entidades: `Usuario` (y subtipos), `Venue`, `Evento`, `Localidad`, `Asiento`, `Oferta`, `Tiquete*`.
  - Servicios de dominio: `PricingPolicy`/`DefaultPricingPolicy`, `OfferEngine`, `TicketFactory`, `PriceBreakdown`.
  - Invariantes: venta y transferencia de `Tiquete`, cálculo de precios y descuentos.
- **Infraestructura**: adaptadores a CSV: `infrastructure.csv.repository.Csv*Repository`, `infrastructure.csv.mappers.*`, utilidades `CSVUtil`, `CsvFormat`, `CsvPaths`.
- **Reglas de dependencia**: Presentación → Aplicación → Dominio. Infraestructura implementa interfaces del dominio y se ensambla desde `AppContext` (anti-corrupción entre capas y wiring centralizado).
- **Diagramas de apoyo**: `arquitectura-componentes.mmd`, `paquetes.mmd`, `despliegue.mmd`.

### Patrones de diseño (concretos)
- **Repository**: interfaces en `domain.repository.*` e implementaciones en `infrastructure.csv.repository.*` (p. ej., `UsuarioRepository` ↔ `CsvUsuarioRepository`). Permite cambiar CSV por otra tecnología sin tocar aplicación/dominio.
- **Strategy**: `PricingPolicy` define el contrato de cálculo de precio; `DefaultPricingPolicy` es la estrategia por defecto. Facilita nuevas políticas (promos, impuestos, etc.).
- **Factory**: `TicketFactory` crea `TiqueteBasico`, `TiqueteNumerado`, `TiqueteAgrupacion`, `TiqueteMultiple`, `PaqueteDeluxe` centralizando la construcción y generación de ids.
- **Composite**: `Tiquete` → `TiqueteBasico` → `TiqueteAgrupacion` → `TiqueteComposite` → (`TiqueteMultiple`, `PaqueteDeluxe`). Permite tratar paquetes y agrupaciones como tiquetes compuestos.
- **Service Layer**: `application.service.*` encapsula casos de uso y coordina transacciones.
- **Value Object**: `PriceBreakdown` es inmutable y describe el resultado del pricing.
- (Infraestructura) **Mapper**: `infrastructure.csv.mappers.*` actúa como traductor a/desde líneas CSV.

### Por qué estos patrones
- Aíslan el dominio de detalles tecnológicos (CSV hoy, DB mañana) y reducen acoplamiento.
- Habilitan extensibilidad: nuevas estrategias de precio, nuevos tipos de tiquete o nuevos repositorios.
- Mejora testabilidad: dominios y servicios se prueban con dobles de `Repository`/`PricingPolicy`.

### Referencias cruzadas
- Arquitectura por capas → `arquitectura-componentes.mmd` y `paquetes.mmd`.
- Patrones de dominio (Strategy/Factory/Composite) → `clases-detallado.mmd` y `clases-alto-nivel.mmd`.
- Flujo de compra y pricing (Strategy en acción) → `actividad-compra.mmd` y `seq-compra.mmd`.

### Reglas de negocio clave
- Autenticación básica con siembra de admin por defecto.
- Compra:
  - Localidad numerada: compra por asiento con reserva del asiento.
  - No numerada: básico/agrupación/deluxe; cálculo de precio = descuentos (ofertas activas) + cargos admin + cuota emisión.
  - Debe existir saldo suficiente del cliente.
- Transferencia: solo propietario, requiere `transferible` y que esté vendido.
- Administración: aprobar/rechazar venues; ajustar cargos de administración.
- Organización: creación de eventos, localidades, asientos y ofertas.

### Supuestos
- Persistencia en archivos CSV locales; no hay concurrencia multi-proceso.
- Consistencia eventual al sobreescribir archivos; atomicidad por archivo con `.tmp`.
- Fechas y montos válidos son provistos por CLI (validaciones mínimas).

### NFR (no funcionales)
- Simplicidad y facilidad de ejecución local (sin DB).
- Tolerancia a ficheros vacíos o inexistentes.
- Legibilidad y mantenibilidad del código Java (paquetes por capas).

### Riesgos y mitigación
- Corrupción de CSV por interrupción: escritura atómica con archivo temporal.
- Escalabilidad limitada: migrable a DB real cambiando únicamente implementaciones de repositorios.
- Integridad referencial: mantenida por lógica de aplicación (consultas previas y validaciones).

### Trazabilidad (ejemplos)
- Requisito “aplicar mejores descuentos” → `OfferEngine`, `OfertaRepository`.
- Requisito “cobrar cargos admin” → `AdminService` (config), `DefaultPricingPolicy` (cálculo).
- Requisito “transferir tiquete” → `TransferService`, `Tiquete.transferirA`.

### Renderizar los diagramas
Puede usar la extensión de Mermaid del IDE o la CLI `mmdc` para exportar a PNG/SVG.


