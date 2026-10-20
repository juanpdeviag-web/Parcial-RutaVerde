package edu.uniquindio.co.rutaverde.patrones.creacional;

import edu.uniquindio.co.rutaverde.model.entities.Actividad;
import edu.uniquindio.co.rutaverde.model.entities.Reserva;
import edu.uniquindio.co.rutaverde.model.entities.Voucher;
import edu.uniquindio.co.rutaverde.model.entities.Producto;
import edu.uniquindio.co.rutaverde.model.services.PagoAndinoAPI;
import edu.uniquindio.co.rutaverde.model.services.PasarelaPagoAdapter;
import edu.uniquindio.co.rutaverde.model.services.ValidadorTarjeta;
import edu.uniquindio.co.rutaverde.util.LuhnValidator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PatronesTest {

    @Test
    void testProductoBuilderBasico() {
        Actividad act1 = new Actividad("A1", "Act1", "Desc", 10, LocalDate.now(), null, new ArrayList<>());
        
        Producto producto = new ProductoBuilder()
            .conId("P1")
            .conNombre("Paquete Test")
            .conDescripcion("Descripción")
            .agregarActividad(act1)
            .conPrecio(100000)
            .build();
        assertEquals("Paquete Test", producto.getNombre());
        assertEquals(100000, producto.getPrecioTotal());
    }

    @Test
    void testProductoBuilderConActividades() {
        Actividad act1 = new Actividad("A1", "Act1", "Desc", 10, LocalDate.now(), null, new ArrayList<>());
        Actividad act2 = new Actividad("A2", "Act2", "Desc", 10, LocalDate.now(), null, new ArrayList<>());
        
        Producto producto = new ProductoBuilder()
            .conId("P1")
            .conNombre("Paquete")
            .agregarActividad(act1)
            .agregarActividad(act2)
            .conPrecio(200000)
            .build();
        
        assertEquals(2, producto.getActividades().size());
    }

    @Test
    void testProductoBuilderSinNombreDebeFallar() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ProductoBuilder()
                .conId("P1")
                .conDescripcion("Desc")
                .build();
        });
    }

    @Test
    void testProductoBuilderSinActividadesDebeFallar() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ProductoBuilder()
                .conId("P1")
                .conNombre("Paquete")
                .conDescripcion("Desc")
                .conPrecio(100000)
                .build();
        });
    }

    @Test
    void testProductoBuilderPrecioNoPositivoDebeFallar() {
        Actividad act1 = new Actividad("A1", "Act1", "Desc", 10, LocalDate.now(), null, new ArrayList<>());
        assertThrows(IllegalArgumentException.class, () -> {
            new ProductoBuilder()
                .conId("P1")
                .conNombre("Paquete")
                .agregarActividad(act1)
                .conPrecio(0)
                .build();
        });
    }

    @Test
    void testVoucherFactoryDetallado() {
        Reserva reserva = new Reserva();
        reserva.setNumero(123);
        
        Voucher voucher = VoucherFactory.crearVoucher("DETALLADO", reserva, null);
        assertEquals("DETALLADO", voucher.getTipo());
        assertEquals("V-123", voucher.getNumero());
    }

    @Test
    void testVoucherFactoryCorto() {
        Reserva reserva = new Reserva();
        reserva.setNumero(456);
        
        Voucher voucher = VoucherFactory.crearVoucher("CORTO", reserva, null);
        assertEquals("CORTO", voucher.getTipo());
    }

    @Test
    void testVoucherFactoryMovil() {
        Reserva reserva = new Reserva();
        reserva.setNumero(789);
        
        Voucher voucher = VoucherFactory.crearVoucher("MOVIL", reserva, null);
        assertEquals("MOVIL", voucher.getTipo());
    }

    @Test
    void testVoucherFactoryTipoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            VoucherFactory.crearVoucher("INVALIDO", null, null);
        });
    }

    @Test
    void testLuhnValidatorTarjetaValida() {
        LuhnValidator validator = new LuhnValidator();
        assertTrue(validator.validar("4539 1488 0343 6467"));
    }

    @Test
    void testLuhnValidatorTarjetaInvalida() {
        LuhnValidator validator = new LuhnValidator();
        assertFalse(validator.validar("4539 1488 0343 6468"));
    }

    @Test
    void testLuhnValidatorConEspacios() {
        LuhnValidator validator = new LuhnValidator();
        assertTrue(validator.validar("4539 1488 0343 6467"));
    }

    @Test
    void testLuhnValidatorSinEspacios() {
        LuhnValidator validator = new LuhnValidator();
        assertTrue(validator.validar("4539148803436467"));
    }

    @Test
    void testLuhnValidatorLongitudInvalida() {
        LuhnValidator validator = new LuhnValidator();
        assertFalse(validator.validar("123"));
    }

    @Test
    void testLuhnValidatorLongitudMuyLarga() {
        LuhnValidator validator = new LuhnValidator();
        assertFalse(validator.validar("12345678901234567890"));
    }

    @Test
    void testLuhnValidatorConLetras() {
        LuhnValidator validator = new LuhnValidator();
        assertFalse(validator.validar("4539 1488 0343 646A"));
    }

    @Test
    void testLuhnValidatorNulo() {
        LuhnValidator validator = new LuhnValidator();
        assertFalse(validator.validar(null));
    }

    @Test
    void testValidadorTarjeta() {
        ValidadorTarjeta validador = new ValidadorTarjeta(new LuhnValidator());
        assertTrue(validador.validar("4539 1488 0343 6467"));
    }

    @Test
    void testValidadorTarjetaInvalida() {
        ValidadorTarjeta validador = new ValidadorTarjeta(new LuhnValidator());
        assertFalse(validador.validar("4539 1488 0343 6468"));
    }

    @Test
    void testValidadorTarjetaLongitudValida() {
        ValidadorTarjeta validador = new ValidadorTarjeta(new LuhnValidator());
        assertTrue(validador.validarLongitud("4539 1488 0343 6467"));
    }

    @Test
    void testValidadorTarjetaLongitudInvalida() {
        ValidadorTarjeta validador = new ValidadorTarjeta(new LuhnValidator());
        assertFalse(validador.validarLongitud("123"));
    }

    @Test
    void testValidadorTarjetaSoloDigitosValido() {
        ValidadorTarjeta validador = new ValidadorTarjeta(new LuhnValidator());
        assertTrue(validador.validarSoloDigitos("4539 1488 0343 6467"));
    }

    @Test
    void testValidadorTarjetaSoloDigitosInvalido() {
        ValidadorTarjeta validador = new ValidadorTarjeta(new LuhnValidator());
        assertFalse(validador.validarSoloDigitos("4539 1488 0343 646A"));
    }

    @Test
    void testPasarelaPagoAdapterExito() {
        PagoAndinoAPI api = new PagoAndinoAPI();
        PasarelaPagoAdapter adapter = new PasarelaPagoAdapter(api);
        int resultado = adapter.procesarPago("4539 1488 0343 6467", 100000, "REF001");
        assertEquals(0, resultado);
    }

    @Test
    void testPasarelaPagoAdapterFondosInsuficientes() {
        PagoAndinoAPI api = new PagoAndinoAPI();
        PasarelaPagoAdapter adapter = new PasarelaPagoAdapter(api);
        int resultado = adapter.procesarPago("4539 1488 0343 6467", 400000000L, "REF001");
        assertEquals(51, resultado);
    }

    @Test
    void testPasarelaPagoAdapterObtenerAutorizacion() {
        PagoAndinoAPI api = new PagoAndinoAPI();
        PasarelaPagoAdapter adapter = new PasarelaPagoAdapter(api);
        adapter.procesarPago("4539 1488 0343 6467", 100000, "REF001");
        String auth = adapter.obtenerAutorizacion();
        assertTrue(auth.startsWith("PA-"));
    }
}
