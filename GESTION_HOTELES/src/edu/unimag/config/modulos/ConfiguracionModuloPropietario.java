package edu.unimag.config.modulos;

import com.cleandev.cli.core.SystemModule;
import com.cleandev.tpa.api.TpaRepository;
import com.cleandev.tpa.api.TpaRepositoryFactory;
import edu.unimag.config.RutaPersistencia;
import edu.unimag.controlador.PropietarioControlador;
import edu.unimag.mapeador.PropietarioMapeador;
import edu.unimag.modelo.Propietario;
import edu.unimag.persistencia.PropietarioRepositorioImpl;
import edu.unimag.repositorio.PropietarioRepositorio;
import edu.unimag.servicio.PropietarioServicio;
import edu.unimag.vista.PropietarioVista;
import java.util.UUID;

public class ConfiguracionModuloPropietario implements ModuloConfigurable {

    private final PropietarioRepositorio repositorio;
    private final PropietarioServicio servicio;

    public ConfiguracionModuloPropietario() {
        TpaRepository<Propietario, UUID> tpaEngine = TpaRepositoryFactory.create(
                Propietario.class,
                RutaPersistencia.PROPIETARIOS.obtenerRuta(),
                true
        );
        repositorio = new PropietarioRepositorioImpl(tpaEngine);
        servicio = new PropietarioServicio(new PropietarioMapeador(), repositorio);
    }

    public PropietarioRepositorio getPropietarioRepositorio() {
        return repositorio;
    }

    public PropietarioServicio getPropietarioServicio() {
        return servicio;
    }

    @Override
    public SystemModule construirVista() {
        return new PropietarioVista(new PropietarioControlador(servicio));
    }

    @Override
    public void cerrarRecursos() {
        if (repositorio != null) {
            repositorio.cerrar();
        }
    }
}