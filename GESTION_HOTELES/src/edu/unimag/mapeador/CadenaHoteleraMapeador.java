package edu.unimag.mapeador;

import edu.unimag.dto.cadenahotelera.CadenaHoteleraDto;
import edu.unimag.modelo.CadenaHotelera;
import java.util.ArrayList;
import java.util.List;

public class CadenaHoteleraMapeador implements Mapeador<CadenaHotelera, CadenaHoteleraDto> {

    @Override
    public CadenaHoteleraDto toDto(CadenaHotelera entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("La cadena hotelera es requerida");
        }
        return new CadenaHoteleraDto(entidad.getIdCadenaHotelera(), entidad.getNombreCadenaHotelera(), entidad.getPaisOrigenCadenaHotelera(), entidad.getNumeroHotelesCadenaHotelera(), entidad.getEstadoCadenaHotelera());
        
    }

    @Override
    public List<CadenaHoteleraDto> toDtoList(List<CadenaHotelera> entidades) {
        if (entidades == null || entidades.isEmpty()) {
            return List.of();
        }
        List<CadenaHoteleraDto> resultado = new ArrayList<>(entidades.size());
        for (CadenaHotelera entidad : entidades) {
            resultado.add(toDto(entidad));
        }
        return resultado;
    }
}
