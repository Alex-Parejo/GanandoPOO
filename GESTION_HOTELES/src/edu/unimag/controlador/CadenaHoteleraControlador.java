package edu.unimag.controlador;

import edu.unimag.dto.cadenahotelera.CadenaHoteleraActualizarDto;
import edu.unimag.dto.cadenahotelera.CadenaHoteleraCrearDto;
import edu.unimag.dto.cadenahotelera.CadenaHoteleraDto;
import edu.unimag.servicio.CadenaHoteleraServicio;
import java.util.List;
import java.util.UUID;

public class CadenaHoteleraControlador {

    private final CadenaHoteleraServicio servicio;

    public CadenaHoteleraControlador(CadenaHoteleraServicio servicio) {
        if (servicio == null) {
            throw new IllegalArgumentException("El servicio de la cadena hotelera no puede ser nulo");
        }
        this.servicio = servicio;
    }

    public CadenaHoteleraDto registrar(CadenaHoteleraCrearDto dto) {
        return servicio.registrarCadenaHotelera(dto);
    }

    public CadenaHoteleraDto actualizar(CadenaHoteleraActualizarDto dto) {
        return servicio.actualizarCadenaHotelera(dto);
    }

    public CadenaHoteleraDto suspender(UUID idCadenaHotelera) {
        return servicio.suspenderCadenaHotelera(idCadenaHotelera);
    }

    public CadenaHoteleraDto reactivar(UUID idCadenaHotelera) {
        return servicio.reactivarCadenaHotelera(idCadenaHotelera);
    }

    public CadenaHoteleraDto buscarPorId(UUID idCadenaHotelera) {
        return servicio.buscarPorId(idCadenaHotelera);
    }

    public List<CadenaHoteleraDto> listarCadenaHotelera() {
        return servicio.listarTodos();
    }

    public int contarCadenasHoteleras() {
        return servicio.contarCadenasHoteleras();
    }
}
