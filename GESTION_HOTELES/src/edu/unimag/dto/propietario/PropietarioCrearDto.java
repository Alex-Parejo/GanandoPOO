package edu.unimag.dto.propietario;

import edu.unimag.dto.validacion.ReglasValidacion;

public record PropietarioCrearDto(
        String nombrePropietario,
        String documentoPropietario,
        String celularPropietario
        ) {

    public PropietarioCrearDto {
        nombrePropietario = ReglasValidacion.limpiarTextoConLongitud(
                nombrePropietario, ReglasValidacion.NOMBRE_MIN, ReglasValidacion.NOMBRE_MAX, "El nombre");
        documentoPropietario = ReglasValidacion.limpiarDocumento(documentoPropietario);
        celularPropietario = ReglasValidacion.limpiarCelular(celularPropietario);
    }

}