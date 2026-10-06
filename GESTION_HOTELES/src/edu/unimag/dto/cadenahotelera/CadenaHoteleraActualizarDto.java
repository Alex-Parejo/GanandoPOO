package edu.unimag.dto.cadenahotelera;

import edu.unimag.dto.validacion.ReglasValidacion;
import java.util.UUID;

public record CadenaHoteleraActualizarDto(
        UUID idCadenaHotelera,
        String nuevoNombre,
        String nuevoPaisOrigen,
        Integer nuevoNumeroHoteles
        ) {

    public CadenaHoteleraActualizarDto {
        idCadenaHotelera = ReglasValidacion.limpiarUuidRequerido(idCadenaHotelera, "Id es obligatorio");

        if (nuevoNombre != null) {
            if (nuevoNombre.isBlank()) {
                nuevoNombre = null;
            } else {
                nuevoNombre = ReglasValidacion.limpiarTextoConLongitud(
                        nuevoNombre, ReglasValidacion.NOMBRE_MIN, ReglasValidacion.NOMBRE_MAX, "El nuevo nombre");
            }
        }

        if (nuevoPaisOrigen != null) {
            if (nuevoPaisOrigen.isBlank()) {
                nuevoPaisOrigen = null;
            } else {
                nuevoPaisOrigen = ReglasValidacion.limpiarTextoConLongitud(
                        nuevoPaisOrigen, ReglasValidacion.PAIS_MIN, ReglasValidacion.PAIS_MAX, "El nuevo pais de origen");
            }
        }

        if (nuevoNumeroHoteles != null) {
            nuevoNumeroHoteles = ReglasValidacion.limpiarEnteroPositivo(
                    nuevoNumeroHoteles, "El nuevo numero de hoteles debe ser mayor que 0");
        }

        if (nuevoNombre == null && nuevoPaisOrigen == null && nuevoNumeroHoteles == null) {
            throw new IllegalArgumentException("No se encuentra nada");
        }

    }
}