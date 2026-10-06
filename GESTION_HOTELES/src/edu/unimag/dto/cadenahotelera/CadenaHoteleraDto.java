package edu.unimag.dto.cadenahotelera;

import edu.unimag.modelo.enumeracion.EstadoEntidad;
import java.util.UUID;

public record CadenaHoteleraDto(
        UUID idCadenaHotelera,
        String nombreCadenaHotelera,
        String paisOrigenCadenaHotelera,
        Integer numeroHotelesCadenaHotelera,
        EstadoEntidad estadoCadenaHotelera
        ) {

}