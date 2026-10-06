package edu.unimag.config.modulos;

import com.cleandev.cli.core.SystemModule;
import com.cleandev.tpa.api.TpaRepository;
import com.cleandev.tpa.api.TpaRepositoryFactory;
import edu.unimag.config.RutaPersistencia;
import edu.unimag.controlador.CadenaHoteleraControlador;
import edu.unimag.mapeador.CadenaHoteleraMapeador;
import edu.unimag.modelo.CadenaHotelera;
import edu.unimag.persistencia.CadenaHoteleraRepositorioImpl;
import edu.unimag.repositorio.CadenaHoteleraRepositorio;
import edu.unimag.servicio.CadenaHoteleraServicio;
import edu.unimag.vista.CadenaHoteleraVista;
import java.util.UUID;

public class ConfiguracionModuloCadenaHotelera implements ModuloConfigurable {

    private final CadenaHoteleraRepositorio repositorio;
    private final CadenaHoteleraServicio servicio;

    public ConfiguracionModuloCadenaHotelera() {
        TpaRepository<CadenaHotelera, UUID> tpaEngine = TpaRepositoryFactory.create(
                CadenaHotelera.class,
                RutaPersistencia.CADENAS_HOTELERAS.obtenerRuta(),
                true
        );
        repositorio = new CadenaHoteleraRepositorioImpl(tpaEngine);
        servicio = new CadenaHoteleraServicio(new CadenaHoteleraMapeador(), repositorio);
    }

    public CadenaHoteleraRepositorio getCadenaHoteleraRepositorio() {
        return repositorio;
    }

    public CadenaHoteleraServicio getCadenaHoteleraServicio() {
        return servicio;
    }

    @Override
    public SystemModule construirVista() {
        return new CadenaHoteleraVista(new CadenaHoteleraControlador(servicio));
    }

    @Override
    public void cerrarRecursos() {
        if (repositorio != null) {
            repositorio.cerrar();
        }
    }
}