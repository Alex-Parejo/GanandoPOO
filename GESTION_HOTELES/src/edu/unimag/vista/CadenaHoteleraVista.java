package edu.unimag.vista;

import com.cleandev.cli.core.SystemModule;
import com.cleandev.cli.io.Console;
import com.cleandev.cli.io.ScreenFormatter;
import com.cleandev.cli.io.TableColumn;
import com.cleandev.cli.io.TableRenderer;
import edu.unimag.controlador.CadenaHoteleraControlador;
import edu.unimag.dto.cadenahotelera.CadenaHoteleraActualizarDto;
import edu.unimag.dto.cadenahotelera.CadenaHoteleraCrearDto;
import edu.unimag.dto.cadenahotelera.CadenaHoteleraDto;
import java.util.List;
import java.util.UUID;

public class CadenaHoteleraVista implements SystemModule {

    private final CadenaHoteleraControlador controlador;
    private final List<TableColumn> columnas;
    private final int anchoTabla;

    public CadenaHoteleraVista(CadenaHoteleraControlador controlador) {
        if (controlador == null) {
            throw new IllegalArgumentException("El controlador de cadena hotelera no puede ser nulo");
        }
        this.controlador = controlador;
        this.columnas = List.of(
                new TableColumn("ID", "%-36s"),
                new TableColumn("Nombre", "%-35s"),
                new TableColumn("Pais de origen", "%-20s"),
                new TableColumn("N. Hoteles", "%10s"),
                new TableColumn("Estado", "%-10s")
        );
        this.anchoTabla = 135;
    }

    @Override
    public String getModuleName() {
        return "Gestion de Cadenas Hoteleras";
    }

    @Override
    public void execute(Console console, ScreenFormatter formatter, TableRenderer renderer) {
        mostrarMenu(console, formatter, renderer);
    }

    private void mostrarMenu(Console console, ScreenFormatter formatter, TableRenderer renderer) {
        boolean ejecutar = true;
        while (ejecutar) {
            int total = controlador.contarCadenasHoteleras();
            console.showMenu(
                    "Modulo de Cadenas Hoteleras",
                    "1. Crear Cadena Hotelera",
                    "2. Listar Cadenas Hoteleras (" + total + ")",
                    "3. Buscar Cadena Hotelera por ID",
                    "4. Actualizar Cadena Hotelera",
                    "5. Suspender Cadena Hotelera",
                    "6. Reactivar Cadena Hotelera",
                    "0. Regresar al menu principal"
            );
            String opcion = console.readText("Seleccione una opcion");
            switch (opcion) {
                case "1" -> crearCadenaHotelera(console, formatter, renderer);
                case "2" -> listarCadenasHoteleras(console, formatter, renderer);
                case "3" -> buscarCadenaHotelera(console, formatter, renderer);
                case "4" -> actualizarCadenaHotelera(console, formatter, renderer);
                case "5" -> suspenderCadenaHotelera(console, formatter, renderer);
                case "6" -> reactivarCadenaHotelera(console, formatter, renderer);
                case "0" -> ejecutar = false;
                default -> console.showError("Opcion incorrecta");
            }
        }
    }

    private void crearCadenaHotelera(Console console, ScreenFormatter formatter, TableRenderer renderer) {
        console.showMessage("\nCrear nueva Cadena Hotelera");
        String nombre = console.readText("Nombre [Enter cancela]");
        if (nombre == null || nombre.isBlank()) {
            console.showMessage("Operacion cancelada");
            return;
        }
        String paisOrigen = console.readText("Pais de origen");
        String numeroTexto = console.readText("Numero de hoteles");
        try {
            Integer numeroHoteles = convertirEntero(numeroTexto, "El numero de hoteles");
            CadenaHoteleraDto respuesta = controlador.registrar(
                    new CadenaHoteleraCrearDto(nombre, paisOrigen, numeroHoteles));
            console.showMessage("\nCadena hotelera creada correctamente");
            mostrarUna(respuesta, formatter, renderer);
        } catch (Exception e) {
            console.showError("Creando cadena hotelera: " + e.getMessage());
        }
    }

    private void listarCadenasHoteleras(Console console, ScreenFormatter formatter, TableRenderer renderer) {
        try {
            List<CadenaHoteleraDto> lista = controlador.listarCadenaHotelera();
            if (lista.isEmpty()) {
                console.showMessage("No hay cadenas hoteleras registradas");
                return;
            }
            console.showMessage("\nListado de Cadenas Hoteleras");
            renderer.render(columnas, lista,
                    cadena -> extraerDatos(cadena, formatter), anchoTabla);
        } catch (Exception e) {
            console.showError("Listando cadenas hoteleras: " + e.getMessage());
        }
    }

    private void buscarCadenaHotelera(Console console, ScreenFormatter formatter, TableRenderer renderer) {
        console.showMessage("\nBuscar Cadena Hotelera por ID");
        UUID id = console.readUUID("ID de la cadena hotelera");
        if (id == null) {
            return;
        }
        try {
            mostrarUna(controlador.buscarPorId(id), formatter, renderer);
        } catch (Exception e) {
            console.showError("Buscando cadena hotelera: " + e.getMessage());
        }
    }

    private void actualizarCadenaHotelera(Console console, ScreenFormatter formatter, TableRenderer renderer) {
        console.showMessage("\nActualizar Cadena Hotelera");
        UUID id = console.readUUID("ID de la cadena hotelera a actualizar");
        if (id == null) {
            return;
        }
        try {
            console.showMessage("\nInformacion actual de la cadena hotelera:");
            mostrarUna(controlador.buscarPorId(id), formatter, renderer);
            String nuevoNombre = console.readText("Nuevo nombre ([Enter] para no cambiar)");
            String nuevoPais = console.readText("Nuevo pais de origen ([Enter] para no cambiar)");
            String nuevoNumeroTexto = console.readText("Nuevo numero de hoteles ([Enter] para no cambiar)");
            Integer nuevoNumero = convertirEntero(nuevoNumeroTexto, "El nuevo numero de hoteles");
            if (nuevoNombre.isBlank() && nuevoPais.isBlank() && nuevoNumero == null) {
                console.showMessage("No se realizaron cambios. Operacion cancelada.");
                return;
            }
            CadenaHoteleraDto respuesta = controlador.actualizar(
                    new CadenaHoteleraActualizarDto(id, nuevoNombre, nuevoPais, nuevoNumero));
            console.showMessage("\nCadena hotelera actualizada correctamente");
            mostrarUna(respuesta, formatter, renderer);
        } catch (Exception e) {
            console.showError("Actualizando cadena hotelera: " + e.getMessage());
        }
    }

    private void suspenderCadenaHotelera(Console console, ScreenFormatter formatter, TableRenderer renderer) {
        console.showMessage("\nSuspender Cadena Hotelera");
        console.showMessage("Las cadenas hoteleras NO se pueden eliminar fisicamente, solo suspender.");
        UUID id = console.readUUID("ID de la cadena hotelera a suspender");
        if (id == null) {
            return;
        }
        try {
            if (!console.readConfirmation("Seguro que desea suspender esta cadena hotelera?")) {
                console.showMessage("Operacion cancelada");
                return;
            }
            CadenaHoteleraDto respuesta = controlador.suspender(id);
            console.showMessage("\nCadena hotelera suspendida correctamente");
            mostrarUna(respuesta, formatter, renderer);
        } catch (Exception e) {
            console.showError("Suspendiendo cadena hotelera: " + e.getMessage());
        }
    }

    private void reactivarCadenaHotelera(Console console, ScreenFormatter formatter, TableRenderer renderer) {
        console.showMessage("\nReactivar Cadena Hotelera");
        UUID id = console.readUUID("ID de la cadena hotelera a reactivar");
        if (id == null) {
            return;
        }
        try {
            CadenaHoteleraDto respuesta = controlador.reactivar(id);
            console.showMessage("\nCadena hotelera reactivada correctamente");
            mostrarUna(respuesta, formatter, renderer);
        } catch (Exception e) {
            console.showError("Reactivando cadena hotelera: " + e.getMessage());
        }
    }

    private Integer convertirEntero(String texto, String nombreCampo) {
        if (texto == null || texto.isBlank()) {
            return null;
        }
        try {
            return Integer.valueOf(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nombreCampo + " debe ser un numero entero");
        }
    }

    private void mostrarUna(CadenaHoteleraDto cadena, ScreenFormatter formatter, TableRenderer renderer) {
        renderer.renderSingle(columnas, cadena,
                dto -> extraerDatos(dto, formatter), anchoTabla);
    }

    private Object[] extraerDatos(CadenaHoteleraDto cadena, ScreenFormatter formatter) {
        String estado = cadena.estadoCadenaHotelera() != null
                ? cadena.estadoCadenaHotelera().getDescription()
                : null;
        return new Object[]{
            String.valueOf(cadena.idCadenaHotelera()),
            cadena.nombreCadenaHotelera(),
            cadena.paisOrigenCadenaHotelera(),
            cadena.numeroHotelesCadenaHotelera(),
            formatter.optionalText(estado)
        };
    }
}
