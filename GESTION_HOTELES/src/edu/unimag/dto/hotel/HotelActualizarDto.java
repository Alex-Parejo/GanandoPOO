package edu.unimag.dto.hotel;

import edu.unimag.dto.validacion.ReglasValidacion;
import java.util.UUID;

public record HotelActualizarDto(
        UUID idHotel,
        String nuevoNombre,
        UUID nuevoIdPropietario,
        UUID nuevoIdCadenaHotelera,
        String nuevoCelular
        ) {

    public HotelActualizarDto {
        idHotel = ReglasValidacion.limpiarUuidRequerido(
                idHotel, "El ID del hotel es obligatorio para actualizar");

        if (nuevoNombre != null) {
            if (nuevoNombre.isBlank()) {
                nuevoNombre = null;
            } else {
                nuevoNombre = ReglasValidacion.limpiarTextoConLongitud(
                        nuevoNombre,
                        ReglasValidacion.NOMBRE_MIN,
                        ReglasValidacion.NOMBRE_MAX,
                        "El nuevo nombre del hotel"
                );
            }
        }

        if (nuevoCelular != null) {
            if (nuevoCelular.isBlank()) {
                nuevoCelular = null;
            } else {
                nuevoCelular = ReglasValidacion.limpiarCelular(nuevoCelular);
            }
        }

        if (nuevoNombre == null && nuevoCelular == null
                && nuevoIdPropietario == null && nuevoIdCadenaHotelera == null) {
            throw new IllegalArgumentException("No viene nada !!!!!!");
        }
    }
}
