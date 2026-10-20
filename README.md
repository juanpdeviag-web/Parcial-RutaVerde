# RutaVerde

Aplicación JavaFX con vistas programáticas, sin FXML. Conserva las entidades,
servicios y patrones del proyecto original.

## Ejecutar

Requiere JDK 21 y Maven. Desde la raíz:

```sh
mvn test
mvn javafx:run
```

En IntelliJ, abre o importa `pom.xml` como proyecto Maven, recarga las dependencias
y selecciona JDK 21. Abre `Main.java`, haz clic derecho y elige **Run 'Main.main()'**.
Las dependencias JavaFX 21 para Windows están configuradas en el POM. También
puedes utilizar el objetivo Maven `javafx:run`. El punto de entrada sigue siendo
`edu.uniquindio.co.rutaverde.Main`.

## Guía dentro de la aplicación

Pulsa **Guía de uso** en Inicio o **Guía** en el menú lateral. Incluye diez
secciones con el flujo completo, índice por temas, accesos directos a las pantallas
y botones para regresar al Inicio. Se puede recorrer con la barra de desplazamiento.

## Demostración

1. En Inicio, pulsa **Cargar ejemplo para la demostración** en una sesión vacía.
2. Explora Clientes, Guías, Actividades y Productos. Selecciona filas para editar.
3. En Reservas, selecciona cliente, producto y fecha de viaje.
4. Usa la tarjeta de prueba `4539 1488 0343 6467` y confirma el pago simulado.
5. Consulta y copia el voucher detallado, corto o móvil.
6. Abre Reportes para consultar ingresos del período y viajeros por producto.

También puedes crear todos los datos desde los formularios. En Productos,
selecciona varias actividades con Ctrl o Mayús; el precio se calcula como la suma
de sus precios. Los importes en los formularios son enteros en COP.

El conjunto demo contiene 10 clientes, 10 guías, 10 actividades, 10 productos y
10 reservas confirmadas. Sus nombres, IDs, precios y relaciones son fijos; las
fechas de actividades comienzan siete días después del día de carga. Los diez pagos
usan la fachada y PagoAndino, emiten vouchers y descuentan cupos. Inicialmente los
ingresos del día son 1.540.000 COP y P001 es el producto más vendido.

Repetir la carga en la misma sesión no duplica registros ni vuelve a cobrar:
una marca de carga en el controller conserva incluso las ediciones y eliminaciones
posteriores. Cierra y vuelve a abrir para comenzar una sesión nueva. Si ya has
registrado datos manualmente, no se mezclan ni sobrescriben con el demo.

Para practicar sin afectar las reservas demo:

- Clientes C009 y C010 no tienen reservas.
- Guías G009 y G010 no están asignados a actividades.
- Actividad A010 no está incluida en productos.
- Productos P007 y P010 no tienen reservas.
- A009 y P009 permiten demostrar el caso sin cupo.

## Alcance

En **Reservas**, pulsa **Agregar tarjeta**, escribe únicamente el número y pulsa
**Guardar tarjeta**. Se validan formato y Luhn con el validador existente. La tarjeta
aparece inmediatamente en **Tarjeta registrada**, identificada por sus últimos
cuatro dígitos, y puede reutilizarse al volver a Reservas. La opción demo sigue
disponible y cargar los ejemplos no elimina ni duplica tarjetas propias.

El pago sigue usando ReservaFacade y PasarelaPagoAdapter para llamar a PagoAndino.
Se conserva la posibilidad de escribir un número directamente en **Tarjeta ***.
Las tarjetas se guardan únicamente en memoria hasta cerrar.

El proyecto previo solo manejaba el número de tarjeta: no existía una entidad de
tarjeta con titular o saldo, ni cuentas de saldo por tarjeta en PagoAndino. Por eso,
el registro no añade campos de titular, saldo o vencimiento ni muestra descuentos
de saldo ficticios. El rechazo por importe de PagoAndino conserva su comportamiento.

- Los datos se comparten entre pantallas y permanecen en memoria hasta cerrar.
- PagoAndino y el envío de correo mantienen la simulación original.
- Cada reserva corresponde a una persona.
- La cancelación conserva el comportamiento del servicio original: cambia el
  estado, sin reembolso ni reposición automática de cupos.
- No se pueden eliminar datos relacionados ni modificar productos o actividades
  que ya tengan reservas. Los IDs permanecen fijos al editar.
- Los ingresos se filtran por fecha de registro de reservas confirmadas; el
  producto más vendido y los viajeros por producto son acumulados de la sesión.

Las pruebas originales se mantienen; las pruebas de integración de controllers
verifican el flujo compartido entre catálogos, fachada de reservas y reportes.
