package edu.uniquindio.co.rutaverde.controller;

import edu.uniquindio.co.rutaverde.model.entities.*;
import edu.uniquindio.co.rutaverde.model.repositories.*;
import edu.uniquindio.co.rutaverde.model.services.*;
import edu.uniquindio.co.rutaverde.patrones.estructural.ReservaFacade;
import edu.uniquindio.co.rutaverde.util.*;
import java.time.LocalDate;
import java.util.*;

/** Dependencias compartidas por todas las pantallas. */
public class MainController {
    private boolean demoCargado;
    public final ClienteRepository clientes = new ClienteRepository();
    public final GuiaRepository guias = new GuiaRepository();
    public final ActividadRepository actividades = new ActividadRepository();
    public final ProductoRepository productos = new ProductoRepository();
    public final TarjetaRepository tarjetas = new TarjetaRepository();
    public final ValidadorTarjeta validador = new ValidadorTarjeta(new LuhnValidator());
    public final PasarelaPagoAdapter pasarela = new PasarelaPagoAdapter(new PagoAndinoAPI());
    public final GestorReservas gestor = new GestorReservas(validador, pasarela);
    public final ReservaRepository reservas = new ReservaRepository(gestor);
    public final Agencia agencia = new Agencia("RutaVerde", "900123456", "12345",
            "Armenia, Quindío", "606 700 0000", "reservas@rutaverde.co", "rutaverde.co");
    public final GeneradorVoucher vouchers = new GeneradorVoucher(agencia);
    public final ReservaFacade facade = new ReservaFacade(validador, pasarela, gestor, vouchers, new EmailService());

    public MainController() {
        new TarjetaController(this).agregar(TarjetaController.NUMERO_DEMO);
    }

    public boolean clienteEnUso(Cliente c) { return reservas.listar().stream().anyMatch(r -> r.getCliente() == c); }
    public boolean guiaEnUso(Guia g) { return actividades.listar().stream().anyMatch(a -> a.getGuia() == g); }
    public boolean actividadEnUso(Actividad a) { return productos.listar().stream().anyMatch(p -> p.getActividades().contains(a)); }
    public boolean productoEnUso(Producto p) { return reservas.listar().stream().anyMatch(r -> r.getProducto() == p); }

    public boolean demoCargado() { return demoCargado; }

    public void cargarEjemplo() {
        if (demoCargado) return;
        if (!clientes.listar().isEmpty() || !guias.listar().isEmpty() || !actividades.listar().isEmpty()
                || !productos.listar().isEmpty() || !reservas.listar().isEmpty())
            throw new IllegalArgumentException("Los ejemplos solo se cargan en una sesión vacía.");
        String[] nombresClientes = {"María López", "Juan Torres", "Laura Gómez", "Carlos Ríos",
                "Diana Martínez", "Felipe Ruiz", "Valentina Castro", "Santiago Mejía", "Camila Vélez", "Mateo Ramírez"};
        String[] correos = {"maria", "juan", "laura", "carlos", "diana", "felipe", "valentina", "santiago", "camila", "mateo"};
        String[] nombresGuias = {"Andrés García", "Lucía Salazar", "Diego Quintero", "Paula Restrepo",
                "Julián Castaño", "Natalia Arias", "Óscar Marín", "Sara Henao", "Esteban Gil", "Daniela Ospina"};
        String[] especialidades = {"Senderismo,Avistamiento", "Café,Cultura", "Aves,Naturaleza",
                "Cicloturismo,Primeros auxilios", "Gastronomía,Cultura", "Ecología,Senderismo",
                "Fotografía,Naturaleza", "Historia,Arquitectura", "Café,Gastronomía", "Senderismo,Interpretación ambiental"};
        for (int i = 0; i < 10; i++) {
            clientes.guardar(new Cliente(id("C", i), nombresClientes[i], String.valueOf(1094001001L + i),
                    correos[i] + "@example.com", String.valueOf(3001234567L + i)));
            guias.guardar(new Guia(id("G", i), nombresGuias[i], String.valueOf(1094002002L + i),
                    new ArrayList<>(Arrays.asList(especialidades[i].split(",")))));
        }
        String[] nombresActividades = {"Sendero del bosque", "Ruta del café en Calarcá", "Aves de Salento",
                "Cicloturismo en Filandia", "Sabores de Armenia", "Caminata por el río Quindío",
                "Fotografía del paisaje cafetero", "Patrimonio de Pijao", "Naturaleza en Buenavista", "Taller de cocina quindiana"};
        String[] descripciones = {"Recorrido guiado por la naturaleza", "Del cultivo a la taza en una finca cafetera",
                "Observación de aves con acompañamiento local", "Recorrido rural en bicicleta por Filandia",
                "Degustación de preparaciones tradicionales", "Caminata ecológica e interpretación del entorno",
                "Recorrido fotográfico por el paisaje cultural cafetero", "Paseo cultural por la arquitectura de Pijao",
                "Caminata entre miradores y cafetales", "Preparación guiada de recetas tradicionales"};
        int[] cupos = {12, 18, 14, 8, 20, 10, 16, 22, 0, 6};
        long[] precios = {120000, 75000, 95000, 180000, 60000, 110000, 90000, 65000, 80000, 55000};
        int[] guiaAsignado = {0, 1, 2, 3, 4, 5, 6, 7, 6, 4};
        LocalDate base = LocalDate.now().plusDays(7);
        for (int i = 0; i < 10; i++) {
            Actividad actividad = new Actividad(id("A", i), nombresActividades[i], descripciones[i], cupos[i],
                    base.plusDays(i), guias.buscar(id("G", guiaAsignado[i])),
                    new ArrayList<>(List.of("Guianza", "Seguro", i % 2 == 0 ? "Refrigerio" : "Material de la experiencia")));
            actividad.setPrecio(precios[i]);
            actividades.guardar(actividad);
        }
        String[] nombresProductos = {"Escapada verde", "Café y aves", "Filandia sobre ruedas", "Bosque y sabores",
                "Río y naturaleza", "Paisajes y alas", "Pijao cultural", "Pueblos y miradores",
                "Buenavista natural", "Paseo patrimonial"};
        int[][] itinerarios = {{0}, {1, 2}, {3}, {0, 4}, {5}, {2, 6}, {7}, {7, 8}, {8}, {7}};
        for (int i = 0; i < 10; i++) {
            var builder = new edu.uniquindio.co.rutaverde.patrones.creacional.ProductoBuilder()
                    .conId(id("P", i)).conNombre(nombresProductos[i])
                    .conDescripcion("Experiencia demo en el Quindío: " + nombresProductos[i]);
            long total = 0;
            for (int indice : itinerarios[i]) {
                Actividad actividad = actividades.buscar(id("A", indice));
                builder.agregarActividad(actividad);
                total += actividad.getPrecio();
            }
            productos.guardar(builder.conPrecio(total).build());
        }
        // Diez cobros reales a la API simulada existente, pasando por la fachada.
        int[] ventas = {0, 0, 0, 1, 1, 2, 3, 4, 5, 5};
        for (int i = 0; i < ventas.length; i++) {
            Producto producto = productos.buscar(id("P", ventas[i]));
            Reserva reserva = facade.confirmarReserva(clientes.buscar(id("C", i % 8)), producto,
                    producto.getActividades().getFirst().getFecha(), TarjetaController.NUMERO_DEMO);
            if (reserva == null) throw new IllegalStateException("No se pudo cargar la reserva demo " + (i + 1));
        }
        demoCargado = true;
    }

    private static String id(String prefijo, int indice) {
        return prefijo + String.format(Locale.ROOT, "%03d", indice + 1);
    }
}
