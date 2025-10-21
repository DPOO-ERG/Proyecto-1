# Boletería CLI (Java + CSV)

Aplicación de consola para gestión de boletería con persistencia en archivos CSV y arquitectura por capas (Presentación, Aplicación, Dominio, Infraestructura). Incluye patrones de diseño: Repositorio, Estrategia (precios), Fábrica (tiquetes) y Compuesto (paquetes/múltiples).

## Requisitos
- Java 17+ (recomendado)
- IDE (IntelliJ) o uso de `javac/java`

## Estructura relevante
- `Proyecto1/src/Main.java`: punto de entrada
- `Proyecto1/src/co/edu/uniandes/proyecto1/presentation/cli/AppCli.java`: CLI
- `Proyecto1/src/co/edu/uniandes/proyecto1/application/service/`: servicios de aplicación
- `Proyecto1/src/co/edu/uniandes/proyecto1/domain/`: modelo de dominio y políticas
- `Proyecto1/src/co/edu/uniandes/proyecto1/infrastructure/csv/`: repositorios CSV, utilidades y mapeadores
- `data/*.csv`: persistencia

## Ejecución rápida (IntelliJ)
1. Abrir el proyecto en IntelliJ.
2. Ejecutar la clase `Main`.
3. Se crearán/actualizarán archivos CSV en `data/` si no existen.

## Usuarios demo (seed)
- ADMIN: login `admin`, password `admin`
- ORGANIZADOR: login `org`, password `org`
- CLIENTE: login `cli`, password `cli` (saldo inicial 500.00)

## Flujos principales en la CLI
- Inicio (registro/login)
- ADMIN
  - Listar venues pendientes, aprobar/rechazar
  - Actualizar cargos (desde servicio; puede agregarse opción en CLI)
- ORGANIZADOR
  - Proponer venue, crear evento, crear localidad, cargar asientos (numeradas), crear oferta
- CLIENTE
  - Ver catálogo (venues aprobados)
  - Comprar básico (localidad no numerada)
  - Comprar numerado (por asiento)
  - Ver mis tiquetes
  - Transferir tiquete (si es transferible)

## Persistencia CSV
- Archivos con encabezado en `data/`: `usuarios.csv`, `venues.csv`, `eventos.csv`, `localidades.csv`, `asientos.csv`, `ofertas.csv`, `tiquetes.csv`, `tiquetes_agregados.csv`
- Escritura atómica (`*.tmp` + replace)

## Notas
- Precios: `subtotal = precioBase * (1 - mejorDescuento)`; `total = subtotal + cargoServicio + cuotaEmision`
- `mejorDescuento` se elige entre ofertas activas para la `Localidad`
- Diseño preparado para futura GUI sin tocar dominio ni repositorios

## Próximos pasos sugeridos
- Añadir compra de agrupación, múltiples y paquete deluxe en CLI
- Pruebas E2E guiadas (aprobación + compra)
- Mejora de validaciones y mensajes


