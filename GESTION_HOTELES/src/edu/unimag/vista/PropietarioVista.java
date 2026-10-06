package edu.unimag.vista;

import com.cleandev.cli.core.SystemModule;
import com.cleandev.cli.io.Console;
import com.cleandev.cli.io.ScreenFormatter;
import com.cleandev.cli.io.TableColumn;
import com.cleandev.cli.io.TableRenderer;
import edu.unimag.controlador.PropietarioControlador;
import edu.unimag.dto.propietario.PropietarioActualizarDto;
import edu.unimag.dto.propietario.PropietarioCrearDto;
import edu.unimag.dto.propietario.PropietarioDto;
import java.util.List;
import java.util.UUID;

public class PropietarioVista implements SystemModule {

    private final PropietarioControlador controlador;
    private final List<TableColumn> columnas;
    private final int anchoTabla;

    public PropietarioVista(PropietarioControlador controlador) {
        if (controlador == null) {
            throw new IllegalArgumentException("El controlador de propietario no puede ser nulo");
        }
        this.controlador = controlador;
        this.columnas = List.of(
                new TableColumn("ID", "%-36s"),
                new TableColumn("Nombre", "%-35s"),
                new TableColumn("Documento", "%-12s"),
                new TableColumn("Celular", "%-10s"),
                new TableColumn("Estado", "%-10s")
        );
        this.anchoTabla = 125;
    }

    @Override
    public String getModuleName() {
        return "Gestion de Propietarios";
    }

    @Override
    public void execute(Console console, ScreenFormatter formatter, TableRenderer renderer) {
        mostrarMenu(console, formatter, renderer);
    }

    private void mostrarMenu(Console console, ScreenFormatter formatter, TableRenderer renderer) {
        boolean ejecutar = true;
        while (ejecutar) {
            int total = controlador.contarPropietarios();
            console.showMenu(
                    "Modulo de Propietarios",
                    "1. Crear Propietario",
                    "2. Listar Propietarios (" + total + ")",
                    "3. Buscar Propietario por ID",
                    "4. Actualizar Propietario",
                    "5. Suspender Propietario",
                    "6. Reactivar Propietario",
                    "0. Regresar al menu principal"
            );
            String opcion = console.readText("Seleccione una opcion");
            switch (opcion) {
                case "1" -> crearPropietario(console, formatter, renderer);
                case "2" -> listarPropietarios(console, formatter, renderer);
                case "3" -> buscarPropietario(console, formatter, renderer);
                case "4" -> actualizarPropietario(console, formatter, renderer);
                case "5" -> suspenderPropietario(console, formatter, renderer);
                case "6" -> reactivarPropietario(console, formatter, renderer);
                case "0" -> ejecutar = false;
                default -> console.showError("Opcion incorrecta");
            }
        }
    }

    private void crearPropietario(Console console, ScreenFormatter formatter, TableRenderer renderer) {
        console.showMessage("\nCrear nuevo Propietario");
        String nombre = console.readText("Nombre [Enter cancela]");
        if (nombre == null || nombre.isBlank()) {
            console.showMessage("Operacion cancelada");
            return;
        }
        String documento = console.readText("Documento");
        String celular = console.readText("Celular");
        try {
            PropietarioDto respuesta = controlador.registrar(
                    new PropietarioCrearDto(nombre, documento, celular));
            console.showMessage("\nPropietario creado correctamente");
            mostrarUno(respuesta, formatter, renderer);
        } catch (Exception e) {
            console.showError("Creando propietario: " + e.getMessage());
        }
    }

    private void listarPropietarios(Console console, ScreenFormatter formatter, TableRenderer renderer) {
        try {
            List<PropietarioDto> lista = controlador.listarPropietario();
            if (lista.isEmpty()) {
                console.showMessage("No hay propietarios registrados");
                return;
            }
            console.showMessage("\nListado de Propietarios");
            renderer.render(columnas, lista,
                    propietario -> extraerDatos(propietario, formatter), anchoTabla);
        } catch (Exception e) {
            console.showError("Listando propietarios: " + e.getMessage());
        }
    }

    private void buscarPropietario(Console console, ScreenFormatter formatter, TableRenderer renderer) {
        console.showMessage("\nBuscar Propietario por ID");
        UUID id = console.readUUID("ID del propietario");
        if (id == null) {
            return;
        }
        try {
            mostrarUno(controlador.buscarPorId(id), formatter, renderer);
        } catch (Exception e) {
            console.showError("Buscando propietario: " + e.getMessage());
        }
    }

    private void actualizarPropietario(Console console, ScreenFormatter formatter, TableRenderer renderer) {
        console.showMessage("\nActualizar Propietario");
        UUID id = console.readUUID("ID del propietario a actualizar");
        if (id == null) {
            return;
        }
        try {
            console.showMessage("\nInformacion actual del propietario:");
            mostrarUno(controlador.buscarPorId(id), formatter, renderer);
            String nuevoNombre = console.readText("Nuevo nombre ([Enter] para no cambiar)");
            String nuevoCelular = console.readText("Nuevo celular ([Enter] para no cambiar)");
            if (nuevoNombre.isBlank() && nuevoCelular.isBlank()) {
                console.showMessage("No se realizaron cambios. Operacion cancelada.");
                return;
            }
            PropietarioDto respuesta = controlador.actualizar(
                    new PropietarioActualizarDto(id, nuevoNombre, nuevoCelular));
            console.showMessage("\nPropietario actualizado correctamente");
            mostrarUno(respuesta, formatter, renderer);
        } catch (Exception e) {
            console.showError("Actualizando propietario: " + e.getMessage());
        }
    }

    private void suspenderPropietario(Console console, ScreenFormatter formatter, TableRenderer renderer) {
        console.showMessage("\nSuspender Propietario");
        console.showMessage("Los propietarios NO se pueden eliminar fisicamente, solo suspender.");
        UUID id = console.readUUID("ID del propietario a suspender");
        if (id == null) {
            return;
        }
        try {
            if (!console.readConfirmation("Seguro que desea suspender este propietario?")) {
                console.showMessage("Operacion cancelada");
                return;
            }
            PropietarioDto respuesta = controlador.suspender(id);
            console.showMessage("\nPropietario suspendido correctamente");
            mostrarUno(respuesta, formatter, renderer);
        } catch (Exception e) {
            console.showError("Suspendiendo propietario: " + e.getMessage());
        }
    }

    private void reactivarPropietario(Console console, ScreenFormatter formatter, TableRenderer renderer) {
        console.showMessage("\nReactivar Propietario");
        UUID id = console.readUUID("ID del propietario a reactivar");
        if (id == null) {
            return;
        }
        try {
            PropietarioDto respuesta = controlador.reactivar(id);
            console.showMessage("\nPropietario reactivado correctamente");
            mostrarUno(respuesta, formatter, renderer);
        } catch (Exception e) {
            console.showError("Reactivando propietario: " + e.getMessage());
        }
    }

    private void mostrarUno(PropietarioDto propietario, ScreenFormatter formatter, TableRenderer renderer) {
        renderer.renderSingle(columnas, propietario,
                dto -> extraerDatos(dto, formatter), anchoTabla);
    }

    private Object[] extraerDatos(PropietarioDto propietario, ScreenFormatter formatter) {
        String estado = propietario.estadoPropietario() != null
                ? propietario.estadoPropietario().getDescription()
                : null;
        return new Object[]{
            String.valueOf(propietario.idPropietario()),
            propietario.nombrePropietario(),
            propietario.documentoPropietario(),
            formatter.optionalText(propietario.celularPropietario()),
            formatter.optionalText(estado)
        };
    }
}
