package edu.unimag.persistencia;

import com.cleandev.tpa.api.TpaRepository;
import edu.unimag.modelo.Hotel;
import edu.unimag.repositorio.HotelRepositorio;
import java.util.UUID;

public class HotelRepositorioImpl
        extends RepositorioBaseAbstracto<Hotel, UUID>
        implements HotelRepositorio {

    public HotelRepositorioImpl(
            TpaRepository<Hotel, UUID> tpaRepository) {

        super(tpaRepository);
    }
}