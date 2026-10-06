package edu.unimag.persistencia;

import com.cleandev.tpa.api.TpaRepository;
import edu.unimag.modelo.CadenaHotelera;
import edu.unimag.repositorio.CadenaHoteleraRepositorio;
import java.util.UUID;

public class CadenaHoteleraRepositorioImpl
        extends RepositorioBaseAbstracto<CadenaHotelera, UUID>
        implements CadenaHoteleraRepositorio {

    public CadenaHoteleraRepositorioImpl(TpaRepository<CadenaHotelera, UUID> tpaRepository) {
        super(tpaRepository);
    }

    @Override
    public boolean insertarSiNombreNoExiste(String nombre, CadenaHotelera cadenaHotelera) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacio");
        }
        if (cadenaHotelera == null) {
            throw new IllegalArgumentException("La cadena hotelera no puede ser nula");
        }
        return tpaRepository.executeAtomic(repo -> {
            for (CadenaHotelera c : repo.findAll()) {
                if (nombre.equalsIgnoreCase(c.getNombreCadenaHotelera())) {
                    return false;
                }
            }
            repo.save(cadenaHotelera);
            return true;
        });
    }

    @Override
    public boolean actualizarSiNoExisteEnOtra(CadenaHotelera cadenaModificada, boolean verificarNombre) {
        if (cadenaModificada == null) {
            throw new IllegalArgumentException("La cadena hotelera no puede ser nula");
        }
        if (cadenaModificada.getIdCadenaHotelera() == null) {
            throw new IllegalArgumentException("El ID de la cadena hotelera no puede ser nulo");
        }
        return tpaRepository.executeAtomic(repo -> {
            UUID id = cadenaModificada.getIdCadenaHotelera();
            CadenaHotelera existente = repo.findById(id).orElse(null);
            if (existente == null) {
                throw new IllegalArgumentException("No existe cadena hotelera con ID: " + id);
            }
            if (verificarNombre) {
                String nombre = cadenaModificada.getNombreCadenaHotelera();
                for (CadenaHotelera c : repo.findAll()) {
                    if (!c.getIdCadenaHotelera().equals(id)
                            && nombre.equalsIgnoreCase(c.getNombreCadenaHotelera())) {
                        return false;
                    }
                }
            }
            repo.update(cadenaModificada);
            return true;
        });
    }
}
