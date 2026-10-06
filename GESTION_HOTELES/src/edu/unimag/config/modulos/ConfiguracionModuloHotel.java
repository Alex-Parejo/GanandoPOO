package edu.unimag.config.modulos;

import com.cleandev.cli.core.SystemModule;
import com.cleandev.tpa.api.TpaRepository;
import com.cleandev.tpa.api.TpaRepositoryFactory;

import edu.unimag.config.RutaPersistencia;
import edu.unimag.controlador.HotelControlador;
import edu.unimag.mapeador.CadenaHoteleraMapeador;
import edu.unimag.mapeador.HotelMapeador;
import edu.unimag.mapeador.PropietarioMapeador;
import edu.unimag.modelo.Hotel;
import edu.unimag.persistencia.HotelRepositorioImpl;
import edu.unimag.repositorio.CadenaHoteleraRepositorio;
import edu.unimag.repositorio.HotelRepositorio;
import edu.unimag.repositorio.PropietarioRepositorio;
import edu.unimag.servicio.HotelServicio;
import edu.unimag.vista.HotelVista;

import java.util.UUID;

public class ConfiguracionModuloHotel
        implements ModuloConfigurable {

    private final HotelRepositorio hotelRepositorio;
    private final PropietarioRepositorio propietarioRepositorio;
    private final CadenaHoteleraRepositorio cadenaHoteleraRepositorio;

    private final HotelServicio hotelServicio;

    public ConfiguracionModuloHotel(
            PropietarioRepositorio propietarioRepositorio,
            CadenaHoteleraRepositorio cadenaHoteleraRepositorio) {

        if (propietarioRepositorio == null) {
            throw new IllegalArgumentException(
                    "El repositorio de propietarios no puede ser nulo");
        }

        if (cadenaHoteleraRepositorio == null) {
            throw new IllegalArgumentException(
                    "El repositorio de cadenas hoteleras no puede ser nulo");
        }

        this.propietarioRepositorio = propietarioRepositorio;
        this.cadenaHoteleraRepositorio = cadenaHoteleraRepositorio;

        boolean necesitaUUID = true;

        TpaRepository<Hotel, UUID> tpaEngine =
                TpaRepositoryFactory.create(
                        Hotel.class,
                        RutaPersistencia.HOTELES.obtenerRuta(),
                        necesitaUUID
                );

        this.hotelRepositorio =
                new HotelRepositorioImpl(tpaEngine);

        PropietarioMapeador propietarioMapeador =
                new PropietarioMapeador();

        CadenaHoteleraMapeador cadenaHoteleraMapeador =
                new CadenaHoteleraMapeador();

        HotelMapeador hotelMapeador =
                new HotelMapeador(
                        propietarioMapeador,
                        cadenaHoteleraMapeador
                );

        this.hotelServicio =
                new HotelServicio(
                        this.hotelRepositorio,
                        this.propietarioRepositorio,
                        this.cadenaHoteleraRepositorio,
                        hotelMapeador
                );
    }

    public HotelRepositorio getHotelRepositorio() {
        return hotelRepositorio;
    }

    public HotelServicio getHotelServicio() {
        return hotelServicio;
    }

    @Override
    public SystemModule construirVista() {

        HotelControlador controller =
                new HotelControlador(this.hotelServicio);

        return new HotelVista(controller);
    }

    @Override
    public void cerrarRecursos() {

        if (hotelRepositorio != null) {
            hotelRepositorio.cerrar();
        }
    }
}