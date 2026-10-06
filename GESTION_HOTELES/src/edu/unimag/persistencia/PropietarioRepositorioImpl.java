package edu.unimag.persistencia;

import com.cleandev.tpa.api.TpaRepository;
import edu.unimag.modelo.Propietario;
import edu.unimag.repositorio.PropietarioRepositorio;
import java.util.UUID;

public class PropietarioRepositorioImpl
        extends RepositorioBaseAbstracto<Propietario, UUID>
        implements PropietarioRepositorio {

    public PropietarioRepositorioImpl(TpaRepository<Propietario, UUID> tpaRepository) {
        super(tpaRepository);
    }

    @Override
    public boolean insertarSiDocumentoNoExiste(String documento, Propietario propietario) {
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("El documento no puede ser nulo ni vacio");
        }
        if (propietario == null) {
            throw new IllegalArgumentException("El propietario no puede ser nulo");
        }
        return tpaRepository.executeAtomic(repo -> {
            for (Propietario p : repo.findAll()) {
                if (documento.equals(p.getDocumentoPropietario())) {
                    return false;
                }
            }
            repo.save(propietario);
            return true;
        });
    }
}
