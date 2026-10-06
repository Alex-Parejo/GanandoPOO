package edu.unimag.dto.hotel;

import edu.unimag.dto.validacion.ReglasValidacion;
import java.util.UUID;

public record HotelCrearDto(
        String nombreHotel,
        UUID idPropietario,
        UUID idCadenaHotelera,
        String celularHotel
        ) {

    public HotelCrearDto {
        nombreHotel = ReglasValidacion.limpiarTextoConLongitud(
                nombreHotel,
                ReglasValidacion.NOMBRE_MIN,
                ReglasValidacion.NOMBRE_MAX,
                "El nombre del hotel"
        );
        idPropietario = ReglasValidacion.limpiarUuidRequerido(
                idPropietario, "El ID del propietario es obligatorio");
        idCadenaHotelera = ReglasValidacion.limpiarUuidRequerido(
                idCadenaHotelera, "El ID de la cadena hotelera es obligatorio");
        celularHotel = ReglasValidacion.limpiarCelular(celularHotel);
    }

}
