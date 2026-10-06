package edu.unimag.repositorio;

import edu.unimag.modelo.CadenaHotelera;
import java.util.UUID;

public interface CadenaHoteleraRepositorio extends RepositorioBase<CadenaHotelera, UUID> {

    boolean insertarSiNombreNoExiste(String nombre, CadenaHotelera cadenaHotelera);

    boolean actualizarSiNoExisteEnOtra(CadenaHotelera cadenaModificada, boolean verificarNombre);
}
