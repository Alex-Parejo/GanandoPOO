package edu.unimag.dto.propietario;

import edu.unimag.modelo.enumeracion.EstadoEntidad;
import java.util.UUID;

public record PropietarioDto(
        UUID idPropietario,
        String nombrePropietario,
        String documentoPropietario,
        String celularPropietario,
        EstadoEntidad estadoPropietario
        ) {

}