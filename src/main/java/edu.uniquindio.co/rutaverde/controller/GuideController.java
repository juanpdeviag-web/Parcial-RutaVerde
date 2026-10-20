package edu.uniquindio.co.rutaverde.controller;

import java.util.List;

/** Instrucciones prácticas para las pantallas disponibles en RutaVerde. */
public class GuideController {
    public record Section(int numero, String titulo, String contenido, String destino) {}

    public List<Section> secciones() {
        return List.of(
            new Section(1, "¿Por dónde empiezo?", """
                Cliente y guía
                ↓
                Actividad
                ↓
                Producto
                ↓
                Reserva
                ↓
                Pago
                ↓
                Voucher
                ↓
                Reportes

                Sigue este orden para preparar tu primer viaje. Elige cada opción desde el menú lateral o utiliza los botones «Ir a…» de esta guía.

                ¿Quieres practicar con ejemplos? Vuelve a Inicio y pulsa «Cargar ejemplo para la demostración» antes de registrar datos propios. Encontrarás clientes, guías, actividades, productos y reservas listos para explorar. Puedes pulsarlo de nuevo sin repetir los ejemplos.

                Los campos con * son obligatorios. Tus datos permanecen mientras la aplicación está abierta; al cerrarla se pierden.
                """, "Inicio"),
            new Section(2, "Registrar un cliente", """
                1. Ingresa a «Clientes» desde el menú lateral.
                2. Pulsa «+ Nuevo» para dejar el formulario listo para un cliente.
                3. Completa «ID *» con un código que no hayas usado, por ejemplo C011.
                4. Escribe «Nombre completo *», «Identificación *», «Correo electrónico *» y «Teléfono *». Revisa que el correo esté bien escrito y que la identificación no esté repetida.
                5. Pulsa «Guardar».
                6. Comprueba el mensaje «Registro guardado correctamente.» y verifica que el cliente aparezca en la tabla.

                Para consultar: escribe el nombre, ID o identificación en «Buscar en el listado…». Borra el texto para volver a ver todos los clientes.

                Para editar: selecciona una fila de la tabla, cambia los datos del formulario y pulsa «Guardar». El ID no se puede cambiar. «Limpiar» deja los campos listos para empezar de nuevo.

                Para eliminar: selecciona el cliente, pulsa «Eliminar» y confirma en la ventana que aparece. Verifica que desaparezca de la tabla. No puedes eliminar un cliente con reservas. Con los ejemplos cargados, C009 y C010 están disponibles para practicar.

                Registra al cliente antes de reservar para poder elegirlo en el campo «Cliente *» de Reservas.
                """, "Clientes"),
            new Section(3, "Registrar un guía", """
                1. Ingresa a «Guías».
                2. Pulsa «+ Nuevo».
                3. Completa «ID *», «Nombre completo *» e «Identificación *». Usa un ID y una identificación que no estén registrados.
                4. Si deseas, escribe «Especialidades, separadas por coma», por ejemplo: Senderismo, Avistamiento.
                5. Pulsa «Guardar».
                6. Comprueba el mensaje «Registro guardado correctamente.» y busca al guía en la tabla.

                Para buscar: usa «Buscar en el listado…» con el nombre, ID, identificación o especialidad.

                Para editar: selecciona la fila, modifica los campos y pulsa «Guardar». El ID permanece igual. Para empezar otro registro, pulsa «+ Nuevo» o «Limpiar».

                Para eliminar: selecciona la fila, pulsa «Eliminar» y confirma. Si el guía ya está asignado a una actividad, no podrás eliminarlo. Con los ejemplos cargados puedes practicar con G009 o G010.

                Registra primero al guía para que después puedas seleccionarlo al crear una actividad.
                """, "Guías"),
            new Section(4, "Crear una actividad", """
                1. Ingresa a «Actividades» y pulsa «+ Nuevo».
                2. Completa «ID *» con un código nuevo y escribe «Nombre *».
                3. Añade una «Descripción» si deseas explicar la experiencia.
                4. En «Fecha *», abre el calendario y selecciona el día de la actividad.
                5. Abre «Guía *» y selecciona un guía registrado. Si no aparece ninguno, ve a Guías, registra uno y regresa a Actividades.
                6. Escribe «Cupos disponibles *»: por ejemplo, 12. Usa un número entero igual o mayor que cero.
                7. Completa «Precio por persona (COP, entero) *»: por ejemplo, 120000, sin símbolos ni puntos. El precio debe ser mayor que cero.
                8. Si corresponde, completa «Servicios, separados por coma», por ejemplo: Guianza, Seguro.
                9. Pulsa «Guardar» y verifica que la actividad aparezca en la tabla.

                Para consultar o modificar: usa «Buscar en el listado…», selecciona una fila, cambia los campos y pulsa «Guardar».

                Para eliminar: selecciona la actividad, pulsa «Eliminar» y confirma. No puedes eliminarla si forma parte de un producto, ni editarla si ya tiene reservas. Puedes practicar con A010 entre los ejemplos.

                Revisa los cupos antes de reservar: una actividad con cero cupos impide reservar los productos que la incluyen. Al confirmar una reserva, verás un cupo menos en cada actividad del producto.
                """, "Actividades"),
            new Section(5, "Crear un producto", """
                1. Ingresa a «Productos» y pulsa «+ Nuevo».
                2. Completa «ID *» con un código nuevo y escribe «Nombre del paquete *».
                3. Añade una «Descripción» si lo deseas.
                4. En «Actividades * (Ctrl o Mayús para seleccionar varias)», selecciona al menos una actividad. Mantén Ctrl mientras haces clic para elegir varias actividades sueltas; usa Mayús para seleccionar un grupo seguido.
                5. Revisa «Total:». Es el precio por persona de todas las actividades seleccionadas juntas. Cambia la selección si necesitas ajustar el paquete.
                6. Pulsa «Guardar».
                7. Verifica que el producto aparezca en la tabla y revisa «Precio por persona» y «Disponibilidad».

                Para buscar: escribe sus datos en «Buscar en el listado…». La columna «Disponibilidad» te permite distinguir «Disponible» de «Sin cupo».

                Para editar: selecciona la fila, cambia nombre, descripción o actividades y pulsa «Guardar». El ID no se cambia. Si el producto ya tiene reservas, crea otro con «+ Nuevo».

                Para eliminar: selecciona el producto, pulsa «Eliminar» y confirma. No podrás eliminar productos con reservas. P007 y P010, entre los ejemplos, permiten practicar edición o eliminación.
                """, "Productos"),
            new Section(6, "Realizar una reserva", """
                1. Ingresa a «Reservas».
                2. En el formulario «Nueva reserva», abre «Cliente *» y selecciona a la persona que viajará.
                3. Abre «Producto *» y selecciona el paquete.
                4. Revisa el precio por persona, los cupos disponibles y las actividades con sus fechas que aparecen debajo. Si no hay cupos, selecciona otro producto.
                5. En «Fecha de viaje *», abre el calendario y elige una fecha de hoy en adelante. Comprueba que sea adecuada para las actividades del paquete.
                6. Revisa el cliente, el producto, las fechas y el precio antes de pasar al pago.

                Cada reserva corresponde a una persona. Para terminarla, selecciona una tarjeta guardada o agrega una nueva y continúa con «Confirmar y pagar», como se explica en el siguiente paso.

                Para consultar una reserva ya realizada, usa «Buscar por número, cliente o producto…» en la parte superior de la tabla. Puedes elegir «CONFIRMADA» o «CANCELADA» en el filtro de estado. Selecciona «Todos los estados» y borra la búsqueda para volver al listado completo.
                """, "Reservas"),
            new Section(7, "Agregar una tarjeta y realizar el pago", """
                Agregar una tarjeta
                1. Ingresa a «Reservas» y busca el texto «Pasarela de pago: PagoAndino».
                2. Pulsa «Agregar tarjeta».
                3. Completa «Número de tarjeta *». Puedes escribir el número con espacios; no se solicitan titular, vencimiento ni otros datos.
                4. Pulsa «Guardar tarjeta».
                5. Comprueba el mensaje «Tarjeta registrada. Ya puedes seleccionarla para pagar.» y revisa «Tarjeta registrada»: tu tarjeta queda seleccionada y se identifica por sus últimos cuatro dígitos.
                6. Si deseas salir del registro sin guardar, pulsa «Cancelar».

                Las tarjetas que agregues permanecen disponibles al cambiar de pantalla y hasta cerrar la aplicación. Cargar los ejemplos no las borra. Si intentas guardar el mismo número otra vez, aparecerá un aviso para que selecciones la tarjeta ya registrada.

                Usar una tarjeta para pagar
                1. En «Reservas», selecciona «Cliente *», «Producto *» y «Fecha de viaje *».
                2. Abre «Tarjeta registrada» y elige tu tarjeta por sus últimos cuatro dígitos. «Tarjeta *» se completa automáticamente y oculta el número. También puedes escribir allí un número para un solo pago, sin guardarlo.
                3. Para usar el ejemplo disponible, elige «Demo · •••• 6467». No es obligatorio usar esa opción: tu tarjeta registrada también sirve.
                4. Revisa el precio y pulsa «Confirmar y pagar». Si no ves el botón, desplázate hacia abajo dentro del formulario.
                5. Lee el mensaje de confirmación: revisa el cliente y el importe. Confirma en la ventana que aparece para continuar, o cancela si necesitas corregir los datos.
                6. Si el pago es exitoso, verás «Pago procesado correctamente mediante PagoAndino», junto al valor pagado, «Estado de la reserva: CONFIRMADA» y «Autorización PagoAndino:».
                7. Verifica que la reserva aparezca en la tabla con estado «CONFIRMADA». Su voucher se muestra debajo del listado. Desplázate dentro del formulario para leer completo el mensaje de pago.

                El pago se realiza mediante PagoAndino. Esta es una demostración y no realiza cobros bancarios reales.

                Si aparece un aviso de error, lee el mensaje y corrige lo indicado antes de intentarlo otra vez. Después de intentar el pago, selecciona nuevamente la tarjeta guardada o vuelve a escribir su número si necesitas reintentar.

                Después de confirmar, verás menos cupos disponibles para el producto y sus actividades. Esta demostración no muestra ni descuenta un saldo por tarjeta; puedes consultar el valor pagado en el mensaje del resultado.
                """, "Reservas"),
            new Section(8, "Consultar el voucher", """
                1. Ingresa a «Reservas».
                2. Busca tu reserva por número, cliente o producto. Si no aparece, revisa el filtro de estado o selecciona «Todos los estados».
                3. Haz clic en la fila de la reserva.
                4. Lee el voucher que aparece en el recuadro debajo de la tabla. Puedes desplazarte dentro de ese recuadro para leerlo completo.
                5. Abre el selector de formato que está junto a «Cancelar reserva» y elige:
                   • «DETALLADO»: información del viaje, cliente, producto, actividades, guías y servicios incluidos.
                   • «CORTO»: resumen de la reserva, producto y fecha.
                   • «MOVIL»: versión compacta para consulta.
                6. Comprueba que el voucher mencione PagoAndino. En DETALLADO y CORTO verás «Pasarela: PagoAndino»; en MOVIL encontrarás «PagoAndino» junto a «pasarela».
                7. Si quieres copiar el contenido, pulsa «Copiar voucher». Verás «Voucher copiado.»; después puedes pegarlo donde lo necesites.

                Cambiar el formato o copiar el voucher no realiza otro pago. Antes de usarlo, revisa también el estado de la reserva en la tabla.
                """, "Reservas"),
            new Section(9, "Consultar reportes", """
                1. Ingresa a «Reportes» desde el menú lateral.
                2. En «Desde» y «Hasta», selecciona las fechas que deseas consultar. La fecha Hasta debe ser igual o posterior a Desde.
                3. Pulsa «Actualizar reporte».
                4. Consulta «Ingresos del período», «Reservas confirmadas» y «Producto más vendido».
                5. Revisa la tabla «Viajeros por producto». Encontrarás «Producto», «Viajeros confirmados» y «Precio actual por persona».
                6. Comprueba que debajo del título aparezca «Pasarela: PagoAndino».

                El rango de fechas corresponde a los ingresos según el día en que hiciste las reservas. «Reservas confirmadas», «Producto más vendido» y «Viajeros por producto» muestran los resultados acumulados mientras mantienes abierta la aplicación.

                Después de hacer o cancelar una reserva, vuelve a Reportes o pulsa «Actualizar reporte» para revisar los resultados. Las reservas canceladas no cuentan como confirmadas.

                Para explorar un reporte con información, carga primero los ejemplos desde Inicio. Sin hacer otros cambios, verás 10 reservas confirmadas y 1.540.000 COP al consultar los ingresos de hoy.
                """, "Reportes"),
            new Section(10, "Cancelar una reserva", """
                1. Ingresa a «Reservas».
                2. Localiza la reserva usando «Buscar por número, cliente o producto…».
                3. Si es necesario, selecciona «CONFIRMADA» en el filtro de estado.
                4. Haz clic en la fila de la reserva que quieres cancelar.
                5. Pulsa «Cancelar reserva».
                6. Lee el aviso y confirma la cancelación en la ventana que aparece.
                7. Selecciona «Todos los estados» o «CANCELADA» en el filtro si la fila dejó de verse.
                8. Comprueba que la columna «Estado» muestre «CANCELADA» y que aparezca el mensaje con el número de reserva cancelada.

                La cancelación no devuelve el dinero ni repone los cupos automáticamente. Solo puedes cancelar una reserva confirmada.

                Para revisar los resultados después de cancelar, ve a «Reportes» y pulsa «Actualizar reporte». Para volver a empezar, utiliza «Inicio» en el menú lateral o «Volver a Inicio» en esta guía.
                """, "Reservas")
        );
    }
}
