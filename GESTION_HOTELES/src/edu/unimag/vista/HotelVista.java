package edu.unimag.vista;

import com.cleandev.cli.core.SystemModule;
import com.cleandev.cli.io.Console;
import com.cleandev.cli.io.ScreenFormatter;
import com.cleandev.cli.io.TableColumn;
import com.cleandev.cli.io.TableRenderer;
import edu.unimag.controlador.HotelControlador;
import edu.unimag.dto.hotel.HotelActualizarDto;
import edu.unimag.dto.hotel.HotelCrearDto;
import edu.unimag.dto.hotel.HotelDto;
import java.util.List;
import java.util.UUID;

public class HotelVista implements SystemModule {

    private final HotelControlador controlador;
    private final List<TableColumn> columnas;
    private final int anchoTabla;

    public HotelVista(HotelControlador controlador) {
        if (controlador == null) {
            throw new IllegalArgumentException(
                    "El controlador no puede ser nulo");
        }
        this.controlador = controlador;
        this.columnas = List.of(
                new TableColumn("ID", "%-36s"),
                new TableColumn("Nombre", "%-30s"),
                new TableColumn("Propietario", "%-25s"),
                new TableColumn("Cadena hotelera", "%-25s"),
                new TableColumn("Celular", "%-10s"),
                new TableColumn("Estado", "%-10s")
        );
        this.anchoTabla = 165;
    }

    @Override
    public String getModuleName() {
        return "Gestion de Hoteles";
    }

    @Override
    public void execute(
            Console console,
            ScreenFormatter formatter,
            TableRenderer renderer) {
        this.mostrarMenu(console, formatter, renderer);
    }

    private void mostrarMenu(
            Console console,
            ScreenFormatter formatter,
            TableRenderer renderer) {
        boolean ejecutar = true;
        while (ejecutar) {
             int total = controlador.contarHoteles();
            console.showMenu(
                    "MODULO DE HOTELES",
                    "1. Registrar Hotel",
                    "2. Listar Todos los Hoteles("+ total +")",
                    "3. Buscar Hotel por ID",
                    "4. Actualizar Hotel",
                    "5. Suspender Hotel",
                    "6. Reactivar Hotel",
                    "0. Regresar al Menu Principal"
            );
            String opcion = console.readText("Seleccione una opcion");
            switch (opcion) {
                case "1" ->
                    registrar(console, formatter, renderer);
                case "2" ->
                    listarTodos(console, formatter, renderer);
                case "3" ->
                    buscarPorId(console, formatter, renderer);
                case "4" ->
                    actualizar(console, formatter, renderer);
                case "5" ->
                    suspender(console, formatter, renderer);
                case "6" ->
                    reactivar(console, formatter, renderer);
                case "0" ->
                    ejecutar = false;
                default ->
                    console.showError("Opcion incorrecta");
            }
        }
    }

    private void registrar(
            Console console,
            ScreenFormatter formatter,
            TableRenderer renderer) {
        console.showMessage("\nRegistrar nuevo Hotel");
        String nombre = console.readText("Nombre del hotel [Enter cancela]");
        if (nombre == null || nombre.isBlank()) {
            console.showMessage("Operacion cancelada");
            return;
        }
        UUID idPropietario = console.readUUID("ID del propietario");
        if (idPropietario == null) {
            return;
        }
        UUID idCadenaHotelera = console.readUUID("ID de la cadena hotelera");
        if (idCadenaHotelera == null) {
            return;
        }
        String celular = console.readText("Celular del hotel");
        try {
            HotelDto response = controlador.registrar(
                    new HotelCrearDto(nombre, idPropietario, idCadenaHotelera, celular));
            console.showMessage("\nHotel registrado correctamente");
            mostrarUno(response, formatter, renderer);
        } catch (IllegalArgumentException | IllegalStateException e) {
            console.showError("Error de validacion: " + e.getMessage());
        } catch (RuntimeException e) {
            console.showError("Error del sistema: " + e.getMessage());
        }
    }

    private void listarTodos(
            Console console,
            ScreenFormatter formatter,
            TableRenderer renderer) {
        try {
            List<HotelDto> lista = controlador.listarTodos();
            if (lista.isEmpty()) {
                console.showMessage("No hay hoteles registrados en el sistema");
                return;
            }
            console.showMessage("\nListado de Hoteles");
            renderer.render(columnas, lista,
                    hotel -> extraerDatos(hotel, formatter), anchoTabla);
        } catch (RuntimeException e) {
            console.showError("Error al listar hoteles: " + e.getMessage());
        }
    }

    private void buscarPorId(
            Console console,
            ScreenFormatter formatter,
            TableRenderer renderer) {
        console.showMessage("\nBuscar Hotel por ID");
        UUID id = console.readUUID("ID del hotel");
        if (id == null) {
            return;
        }
        try {
            mostrarUno(controlador.buscarPorId(id), formatter, renderer);
        } catch (IllegalArgumentException | IllegalStateException e) {
            console.showError("Error de validacion: " + e.getMessage());
        } catch (RuntimeException e) {
            console.showError("Error del sistema: " + e.getMessage());
        }
    }

    private void actualizar(
            Console console,
            ScreenFormatter formatter,
            TableRenderer renderer) {
        console.showMessage("\nActualizar Hotel");
        UUID id = console.readUUID("ID del hotel a actualizar");
        if (id == null) {
            return;
        }
        try {
            HotelDto hotelActual = controlador.buscarPorId(id);
            console.showMessage("\nInformacion actual del hotel:");
            mostrarUno(hotelActual, formatter, renderer);

            String nuevoNombre = console.readText("Nuevo nombre ([Enter] para no cambiar)");
            if (nuevoNombre.isBlank()) {
                nuevoNombre = null;
            }
            String nuevoCelular = console.readText("Nuevo celular ([Enter] para no cambiar)");
            if (nuevoCelular.isBlank()) {
                nuevoCelular = null;
            }
            UUID nuevoIdPropietario = null;
            if (console.readConfirmation("Desea cambiar el propietario?")) {
                nuevoIdPropietario = solicitarCambioPropietario(console, hotelActual);
                if (nuevoIdPropietario == null) {
                    return;
                }
            }
            UUID nuevoIdCadenaHotelera = null;
            if (console.readConfirmation("Desea cambiar la cadena hotelera?")) {
                nuevoIdCadenaHotelera = solicitarCambioCadenaHotelera(console, hotelActual);
                if (nuevoIdCadenaHotelera == null) {
                    return;
                }
            }
            if (nuevoNombre == null
                    && nuevoCelular == null
                    && nuevoIdPropietario == null
                    && nuevoIdCadenaHotelera == null) {
                console.showMessage("No se realizaron cambios. Operacion cancelada.");
                return;
            }
            HotelDto response = controlador.actualizar(
                    new HotelActualizarDto(
                            id,
                            nuevoNombre,
                            nuevoIdPropietario,
                            nuevoIdCadenaHotelera,
                            nuevoCelular
                    ));
            console.showMessage("\nHotel actualizado correctamente");
            mostrarUno(response, formatter, renderer);
        } catch (IllegalArgumentException | IllegalStateException e) {
            console.showError("Error de validacion: " + e.getMessage());
        } catch (RuntimeException e) {
            console.showError("Error del sistema: " + e.getMessage());
        }
    }

    private void suspender(
            Console console,
            ScreenFormatter formatter,
            TableRenderer renderer) {
        console.showMessage("\nSuspender Hotel");
        console.showMessage("ADVERTENCIA: Esta accion desactivara el hotel.");
        console.showMessage("Los hoteles NO se pueden eliminar fisicamente, solo suspender.");
        UUID id = console.readUUID("ID del hotel a suspender");
        if (id == null) {
            return;
        }
        try {
            if (!console.readConfirmation("SEGURO que desea suspender este hotel?")) {
                console.showMessage("Operacion cancelada por el usuario");
                return;
            }
            HotelDto response = controlador.suspender(id);
            console.showMessage("\nHotel suspendido correctamente");
            mostrarUno(response, formatter, renderer);
        } catch (IllegalArgumentException | IllegalStateException e) {
            console.showError("Error de validacion: " + e.getMessage());
        } catch (RuntimeException e) {
            console.showError("Error del sistema: " + e.getMessage());
        }
    }

    private void reactivar(
            Console console,
            ScreenFormatter formatter,
            TableRenderer renderer) {
        console.showMessage("\nReactivar Hotel");
        UUID id = console.readUUID("ID del hotel a reactivar");
        if (id == null) {
            return;
        }
        try {
            HotelDto response = controlador.reactivar(id);
            console.showMessage("\nHotel reactivado correctamente");
            mostrarUno(response, formatter, renderer);
        } catch (IllegalArgumentException | IllegalStateException e) {
            console.showError("Error de validacion: " + e.getMessage());
        } catch (RuntimeException e) {
            console.showError("Error del sistema: " + e.getMessage());
        }
    }

    private UUID solicitarCambioPropietario(
            Console console,
            HotelDto hotelActual) {
        console.showMessage("\nPropietario actual: "
                + hotelActual.propietario().nombrePropietario());
        UUID nuevoId = console.readUUID("Nuevo ID de propietario ([Enter] para cancelar)");
        if (nuevoId == null) {
            return null;
        }
        if (nuevoId.equals(hotelActual.propietario().idPropietario())) {
            console.showMessage("El propietario es el mismo. No hay cambios.");
            return null;
        }
        console.showMessage("\nEsta a punto de mover:");
        console.showMessage("    Hotel: " + hotelActual.nombreHotel());
        console.showMessage("    De: " + hotelActual.propietario().nombrePropietario());
        console.showMessage("    A: Propietario con ID " + nuevoId);
        if (!console.readConfirmation("Continuar con el cambio?")) {
            console.showMessage("Cambio de propietario cancelado.");
            return null;
        }
        return nuevoId;
    }

    private UUID solicitarCambioCadenaHotelera(
            Console console,
            HotelDto hotelActual) {
        console.showMessage("\nCadena hotelera actual: "
                + hotelActual.cadenaHotelera().nombreCadenaHotelera());
        UUID nuevoId = console.readUUID("Nuevo ID de cadena hotelera ([Enter] para cancelar)");
        if (nuevoId == null) {
            return null;
        }
        if (nuevoId.equals(hotelActual.cadenaHotelera().idCadenaHotelera())) {
            console.showMessage("La cadena hotelera es la misma. No hay cambios.");
            return null;
        }
        console.showMessage("\nEsta a punto de mover:");
        console.showMessage("    Hotel: " + hotelActual.nombreHotel());
        console.showMessage("    De: " + hotelActual.cadenaHotelera().nombreCadenaHotelera());
        console.showMessage("    A: Cadena hotelera con ID " + nuevoId);
        if (!console.readConfirmation("Continuar con el cambio?")) {
            console.showMessage("Cambio de cadena hotelera cancelado.");
            return null;
        }
        return nuevoId;
    }

    private void mostrarUno(
            HotelDto hotel,
            ScreenFormatter formatter,
            TableRenderer renderer) {
        renderer.renderSingle(columnas, hotel,
                dto -> extraerDatos(dto, formatter), anchoTabla);
    }

    private Object[] extraerDatos(HotelDto hotel, ScreenFormatter formatter) {
        String estado = hotel.estadoHotel() != null
                ? hotel.estadoHotel().getDescription()
                : null;
        return new Object[]{
            String.valueOf(hotel.idHotel()),
            hotel.nombreHotel(),
            hotel.propietario().nombrePropietario(),
            hotel.cadenaHotelera().nombreCadenaHotelera(),
            formatter.optionalText(hotel.celularHotel()),
            formatter.optionalText(estado)
        };
    }
}
