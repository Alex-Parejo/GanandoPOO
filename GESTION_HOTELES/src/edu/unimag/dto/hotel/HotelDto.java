package edu.unimag.dto.hotel;

import edu.unimag.dto.cadenahotelera.CadenaHoteleraDto;
import edu.unimag.dto.propietario.PropietarioDto;
import edu.unimag.modelo.enumeracion.EstadoEntidad;
import java.util.UUID;

public record HotelDto(
        UUID idHotel,
        String nombreHotel,
        PropietarioDto propietario,
        CadenaHoteleraDto cadenaHotelera,
        String celularHotel,
        EstadoEntidad estadoHotel
        ) {

}
