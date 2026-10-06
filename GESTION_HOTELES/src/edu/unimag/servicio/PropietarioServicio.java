package edu.unimag.servicio;

import edu.unimag.dto.propietario.PropietarioActualizarDto;
import edu.unimag.dto.propietario.PropietarioCrearDto;
import edu.unimag.dto.propietario.PropietarioDto;
import edu.unimag.dto.validacion.ReglasValidacion;
import edu.unimag.mapeador.PropietarioMapeador;
import edu.unimag.modelo.Propietario;
import edu.unimag.repositorio.PropietarioRepositorio;
import java.util.List;
import java.util.UUID;

public class PropietarioServicio {

    private final PropietarioMapeador mapper;
    private final PropietarioRepositorio repositorio;

    public PropietarioServicio(PropietarioMapeador mapper, PropietarioRepositorio repositorio) {
        if (repositorio == null) {
            throw new IllegalArgumentException("La persistencia del propietario no puede ser nula");
        }
        if (mapper == null) {
            throw new IllegalArgumentException("El mapper del propietario no puede ser nulo");
        }
        this.mapper = mapper;
        this.repositorio = repositorio;
    }

    public PropietarioDto registrarPropietario(PropietarioCrearDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Los datos del propietario son requeridos");
        }
        Propietario nuevo = new Propietario(
                dto.nombrePropietario(),
                dto.documentoPropietario(),
                dto.celularPropietario()
        );
        boolean insertado = repositorio.insertarSiDocumentoNoExiste(dto.documentoPropietario(), nuevo);
        ReglasValidacion.validarUnico(
                !insertado,
                "Ya existe un propietario con el documento: " + dto.documentoPropietario()
        );
        return mapper.toDto(nuevo);
    }

    public PropietarioDto actualizarPropietario(PropietarioActualizarDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Los datos del propietario son requeridos");
        }
        Propietario propietario = ReglasValidacion.validarIdExiste(
                repositorio.buscarPorId(dto.idPropietario()), dto.idPropietario());
        if (dto.nuevoNombre() != null) {
            propietario.actualizarNombre(dto.nuevoNombre());
        }
        if (dto.nuevoCelular() != null) {
            propietario.actualizarCelular(dto.nuevoCelular());
        }
        return mapper.toDto(repositorio.actualizar(propietario));
    }

    public PropietarioDto suspenderPropietario(UUID idPropietario) {
        Propietario propietario = ReglasValidacion.validarIdExiste(
                repositorio.buscarPorId(idPropietario), idPropietario);
        ReglasValidacion.validarPuedeSuspender(propietario.getEstadoPropietario());
        propietario.suspender();
        return mapper.toDto(repositorio.actualizar(propietario));
    }

    public PropietarioDto reactivarPropietario(UUID idPropietario) {
        Propietario propietario = ReglasValidacion.validarIdExiste(
                repositorio.buscarPorId(idPropietario), idPropietario);
        ReglasValidacion.validarPuedeReactivar(propietario.getEstadoPropietario());
        propietario.reactivar();
        return mapper.toDto(repositorio.actualizar(propietario));
    }

    public PropietarioDto buscarPorId(UUID idPropietario) {
        Propietario propietario = ReglasValidacion.validarIdExiste(
                repositorio.buscarPorId(idPropietario), idPropietario);
        return mapper.toDto(propietario);
    }

    public List<PropietarioDto> listarTodos() {
        return mapper.toDtoList(repositorio.listarTodos());
    }

    public int contarPropietarios() {
        return (int) repositorio.contar();
    }
}
