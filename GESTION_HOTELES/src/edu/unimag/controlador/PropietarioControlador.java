package edu.unimag.controlador;

import edu.unimag.dto.propietario.PropietarioActualizarDto;
import edu.unimag.dto.propietario.PropietarioCrearDto;
import edu.unimag.dto.propietario.PropietarioDto;
import edu.unimag.servicio.PropietarioServicio;
import java.util.List;
import java.util.UUID;

public class PropietarioControlador {

    private final PropietarioServicio servicio;

    public PropietarioControlador(PropietarioServicio servicio) {
        if (servicio == null) {
            throw new IllegalArgumentException("El servicio de propietario no puede ser nulo");
        }
        this.servicio = servicio;
    }

    public PropietarioDto registrar(PropietarioCrearDto dto) {
        return servicio.registrarPropietario(dto);
    }

    public PropietarioDto actualizar(PropietarioActualizarDto dto) {
        return servicio.actualizarPropietario(dto);
    }

    public PropietarioDto suspender(UUID idPropietario) {
        return servicio.suspenderPropietario(idPropietario);
    }

    public PropietarioDto reactivar(UUID idPropietario) {
        return servicio.reactivarPropietario(idPropietario);
    }

    public PropietarioDto buscarPorId(UUID idPropietario) {
        return servicio.buscarPorId(idPropietario);
    }

    public List<PropietarioDto> listarPropietario() {
        return servicio.listarTodos();
    }

    public int contarPropietarios() {
        return servicio.contarPropietarios();
    }
}
