package edu.unimag.config.modulos;

import com.cleandev.cli.core.SystemModule;

public interface ModuloConfigurable {

    SystemModule construirVista();
    void cerrarRecursos();
}
