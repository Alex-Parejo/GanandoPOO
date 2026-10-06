package edu.unimag.repositorio;

import edu.unimag.modelo.Propietario;
import java.util.UUID;

public interface PropietarioRepositorio extends RepositorioBase<Propietario, UUID> {

    boolean insertarSiDocumentoNoExiste(String documento, Propietario propietario);
}
