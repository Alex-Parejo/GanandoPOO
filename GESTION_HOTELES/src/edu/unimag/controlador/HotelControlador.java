package edu.unimag.controlador;

import edu.unimag.dto.hotel.HotelActualizarDto;
import edu.unimag.dto.hotel.HotelCrearDto;
import edu.unimag.dto.hotel.HotelDto;
import edu.unimag.servicio.HotelServicio;

import java.util.List;
import java.util.UUID;

public class HotelControlador {

    private final HotelServicio servicio;

    public HotelControlador(HotelServicio servicio) {

        if (servicio == null) {
            throw new IllegalArgumentException(
                    "El servicio de hoteles no puede ser nulo");
        }

        this.servicio = servicio;
    }

    public HotelDto registrar(HotelCrearDto request) {
        return servicio.registrar(request);
    }

    public HotelDto actualizar(HotelActualizarDto request) {
        return servicio.actualizar(request);
    }

    public HotelDto suspender(UUID idHotel) {
        return servicio.suspender(idHotel);
    }

    public HotelDto reactivar(UUID idHotel) {
        return servicio.reactivar(idHotel);
    }

    public List<HotelDto> listarTodos() {
        return servicio.listarTodos();
    }

    public HotelDto buscarPorId(UUID idHotel) {
        return servicio.buscarPorId(idHotel);
    }
    
      public int contarHoteles() {
        return servicio.contarHoteles();
    }
    
    
    
}