package edu.unimag.config;

import com.cleandev.cli.core.SystemModule;

import edu.unimag.config.modulos.ConfiguracionModuloCadenaHotelera;
import edu.unimag.config.modulos.ConfiguracionModuloHotel;

import edu.unimag.config.modulos.ConfiguracionModuloPropietario;
import edu.unimag.config.modulos.ModuloConfigurable;

import java.util.ArrayList;
import java.util.List;

public class ConfiguracionDependencias
        implements AutoCloseable {

    private final List<ModuloConfigurable> modulosConfigurados =
            new ArrayList<>();

    private final List<SystemModule> modulos =
            new ArrayList<>();

    public ConfiguracionDependencias() {

        ConfiguracionModuloPropietario moduloPropietario = new ConfiguracionModuloPropietario();
        registrar(moduloPropietario);

        ConfiguracionModuloCadenaHotelera moduloCadenaHotelera = new ConfiguracionModuloCadenaHotelera();
        registrar(moduloCadenaHotelera);

        ConfiguracionModuloHotel moduloHotel =new ConfiguracionModuloHotel(moduloPropietario.getPropietarioRepositorio(), moduloCadenaHotelera.getCadenaHoteleraRepositorio());
        registrar(moduloHotel);
    }

    private void registrar(ModuloConfigurable configuracion) {

        modulosConfigurados.add(configuracion);
        modulos.add(configuracion.construirVista());
    }

    public List<SystemModule> getModulos() {
        return List.copyOf(modulos);
    }

    @Override
    public void close() {

        for (int i = modulosConfigurados.size() - 1;
                i >= 0;
                i--) {

            ModuloConfigurable modulo =
                    modulosConfigurados.get(i);

            try {
                modulo.cerrarRecursos();
            } catch (Exception e) {
                System.err.println(
                        "Error cerrando recursos: "
                        + e.getMessage()
                );
            }
        }

        modulosConfigurados.clear();
        modulos.clear();
    }
}