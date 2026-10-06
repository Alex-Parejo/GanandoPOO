package edu.unimag.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public enum RutaPersistencia {

    PROPIETARIOS("propietarios.txt"),
    CADENAS_HOTELERAS("cadenashoteleras.txt"),
    HOTELES("hoteles.txt");

    private final Path ruta;

    RutaPersistencia(String nombreArchivo) {
        ruta = Paths.get(
                System.getProperty("user.dir"),
                "misPersistencias",
                nombreArchivo
        );
    }

    public Path obtenerRuta() {
        return ruta;
    }
}