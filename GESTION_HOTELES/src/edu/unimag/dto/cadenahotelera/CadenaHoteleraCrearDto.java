package edu.unimag.dto.cadenahotelera;

import edu.unimag.dto.validacion.ReglasValidacion;

public record CadenaHoteleraCrearDto(
        String nombreCadenaHotelera,
        String paisOrigenCadenaHotelera,
        Integer numeroHotelesCadenaHotelera
        ) {

    public CadenaHoteleraCrearDto {
        nombreCadenaHotelera = ReglasValidacion.limpiarTextoConLongitud(
                nombreCadenaHotelera, ReglasValidacion.NOMBRE_MIN, ReglasValidacion.NOMBRE_MAX, "El nombre");
        paisOrigenCadenaHotelera = ReglasValidacion.limpiarTextoConLongitud(
                paisOrigenCadenaHotelera, ReglasValidacion.PAIS_MIN, ReglasValidacion.PAIS_MAX, "El pais de origen");
        numeroHotelesCadenaHotelera = ReglasValidacion.limpiarEnteroPositivo(
                numeroHotelesCadenaHotelera, "El numero de hoteles debe ser mayor que 0");
    }

}