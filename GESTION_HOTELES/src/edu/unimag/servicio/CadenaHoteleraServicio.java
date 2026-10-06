package edu.unimag.servicio;

import edu.unimag.dto.cadenahotelera.CadenaHoteleraActualizarDto;
import edu.unimag.dto.cadenahotelera.CadenaHoteleraCrearDto;
import edu.unimag.dto.cadenahotelera.CadenaHoteleraDto;
import edu.unimag.dto.validacion.ReglasValidacion;
import edu.unimag.mapeador.CadenaHoteleraMapeador;
import edu.unimag.modelo.CadenaHotelera;
import edu.unimag.repositorio.CadenaHoteleraRepositorio;
import java.util.List;
import java.util.UUID;

public class CadenaHoteleraServicio {

    private final CadenaHoteleraMapeador mapper;
    private final CadenaHoteleraRepositorio repositorio;

    public CadenaHoteleraServicio(CadenaHoteleraMapeador mapper, CadenaHoteleraRepositorio repositorio) {
        if (repositorio == null) {
            throw new IllegalArgumentException("La persistencia de la cadena hotelera no puede ser nula");
        }
        if (mapper == null) {
            throw new IllegalArgumentException("El mapper de la cadena hotelera no puede ser nulo");
        }
        this.mapper = mapper;
        this.repositorio = repositorio;
    }

    public CadenaHoteleraDto registrarCadenaHotelera(CadenaHoteleraCrearDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Los datos de la cadena hotelera son requeridos");
        }
        CadenaHotelera nueva = new CadenaHotelera(
                dto.nombreCadenaHotelera(),
                dto.paisOrigenCadenaHotelera(),
                dto.numeroHotelesCadenaHotelera()
        );
        boolean insertada = repositorio.insertarSiNombreNoExiste(dto.nombreCadenaHotelera(), nueva);
        ReglasValidacion.validarUnico(
                !insertada,
                "Ya existe una cadena hotelera con el nombre: " + dto.nombreCadenaHotelera()
        );
        return mapper.toDto(nueva);
    }

    public CadenaHoteleraDto actualizarCadenaHotelera(CadenaHoteleraActualizarDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Los datos de la cadena hotelera son requeridos");
        }
        CadenaHotelera cadena = ReglasValidacion.validarIdExiste(
                repositorio.buscarPorId(dto.idCadenaHotelera()), dto.idCadenaHotelera());
        boolean cambioNombre = dto.nuevoNombre() != null;
        if (cambioNombre) {
            cadena.actualizarNombre(dto.nuevoNombre());
        }
        if (dto.nuevoPaisOrigen() != null) {
            cadena.actualizarPaisOrigen(dto.nuevoPaisOrigen());
        }
        if (dto.nuevoNumeroHoteles() != null) {
            cadena.actualizarNumeroHoteles(dto.nuevoNumeroHoteles());
        }
        boolean actualizada = repositorio.actualizarSiNoExisteEnOtra(cadena, cambioNombre);
        ReglasValidacion.validarUnico(
                !actualizada,
                "Ya existe otra cadena hotelera con el nombre: " + dto.nuevoNombre()
        );
        return mapper.toDto(cadena);
    }

    public CadenaHoteleraDto suspenderCadenaHotelera(UUID idCadenaHotelera) {
        CadenaHotelera cadena = ReglasValidacion.validarIdExiste(
                repositorio.buscarPorId(idCadenaHotelera), idCadenaHotelera);
        ReglasValidacion.validarPuedeSuspender(cadena.getEstadoCadenaHotelera());
        cadena.suspender();
        return mapper.toDto(repositorio.actualizar(cadena));
    }

    public CadenaHoteleraDto reactivarCadenaHotelera(UUID idCadenaHotelera) {
        CadenaHotelera cadena = ReglasValidacion.validarIdExiste(
                repositorio.buscarPorId(idCadenaHotelera), idCadenaHotelera);
        ReglasValidacion.validarPuedeReactivar(cadena.getEstadoCadenaHotelera());
        cadena.reactivar();
        return mapper.toDto(repositorio.actualizar(cadena));
    }

    public CadenaHoteleraDto buscarPorId(UUID idCadenaHotelera) {
        CadenaHotelera cadena = ReglasValidacion.validarIdExiste(
                repositorio.buscarPorId(idCadenaHotelera), idCadenaHotelera);
        return mapper.toDto(cadena);
    }

    public List<CadenaHoteleraDto> listarTodos() {
        return mapper.toDtoList(repositorio.listarTodos());
    }

    public int contarCadenasHoteleras() {
        return (int) repositorio.contar();
    }
}
