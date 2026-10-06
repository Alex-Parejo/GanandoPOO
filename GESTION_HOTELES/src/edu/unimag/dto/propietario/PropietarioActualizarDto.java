package edu.unimag.dto.propietario;

import edu.unimag.dto.validacion.ReglasValidacion;
import java.util.UUID;

public record PropietarioActualizarDto(
        UUID idPropietario,
        String nuevoNombre,
        String nuevoCelular
        ) {

    public PropietarioActualizarDto {
        idPropietario = ReglasValidacion.limpiarUuidRequerido(idPropietario, "Id es obligatorio");

        if (nuevoNombre != null) {
            if (nuevoNombre.isBlank()) {
                nuevoNombre = null;
            } else {
                nuevoNombre = ReglasValidacion.limpiarTextoConLongitud(
                        nuevoNombre, ReglasValidacion.NOMBRE_MIN, ReglasValidacion.NOMBRE_MAX, "El nuevo nombre");
            }
        }

        if (nuevoCelular != null) {
            if (nuevoCelular.isBlank()) {
                nuevoCelular = null;
            } else {
                nuevoCelular = ReglasValidacion.limpiarCelular(nuevoCelular);
            }
        }

        if (nuevoNombre == null && nuevoCelular == null) {
            throw new IllegalArgumentException("No viene nada !!!!!!");
        }

    }
}
