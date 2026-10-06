package edu.unimag.mapeador;

import edu.unimag.dto.propietario.PropietarioDto;
        
import edu.unimag.modelo.Propietario;
import java.util.ArrayList;
import java.util.List;

public class PropietarioMapeador implements Mapeador<Propietario, PropietarioDto> {

    @Override
    public PropietarioDto toDto(Propietario entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("El propietario es requerido");
        }
        return new PropietarioDto(entidad.getIdPropietario(),entidad.getNombrePropietario(), entidad.getDocumentoPropietario(),entidad.getCelularPropietario(), entidad.getEstadoPropietario());
        
    }

    @Override
    public List<PropietarioDto> toDtoList(List<Propietario> entidades) {
        if (entidades == null || entidades.isEmpty()) {
            return List.of();
        }
        List<PropietarioDto> resultado = new ArrayList<>(entidades.size());
        for (Propietario entidad : entidades) {
            resultado.add(toDto(entidad));
        }
        return resultado;
    }
}
