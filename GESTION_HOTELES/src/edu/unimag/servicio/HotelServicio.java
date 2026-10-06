package edu.unimag.servicio;

import edu.unimag.dto.hotel.HotelActualizarDto;
import edu.unimag.dto.hotel.HotelCrearDto;
import edu.unimag.dto.hotel.HotelDto;
import edu.unimag.dto.validacion.ReglasValidacion;
import edu.unimag.mapeador.HotelMapeador;
import edu.unimag.modelo.CadenaHotelera;
import edu.unimag.modelo.Hotel;
import edu.unimag.modelo.Propietario;
import edu.unimag.repositorio.CadenaHoteleraRepositorio;
import edu.unimag.repositorio.HotelRepositorio;
import edu.unimag.repositorio.PropietarioRepositorio;
import edu.unimag.repositorio.cargador.CadenaHoteleraCargador;
import edu.unimag.repositorio.cargador.PropietarioCargador;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class HotelServicio {

    private final HotelRepositorio hotelRepositorio;
    private final PropietarioRepositorio propietarioRepositorio;
    private final CadenaHoteleraRepositorio cadenaHoteleraRepositorio;
    private final HotelMapeador hotelMapeador;

    public HotelServicio(
            HotelRepositorio hotelRepositorio,
            PropietarioRepositorio propietarioRepositorio,
            CadenaHoteleraRepositorio cadenaHoteleraRepositorio,
            HotelMapeador hotelMapeador) {
        if (hotelRepositorio == null) {
            throw new IllegalArgumentException(
                    "El repositorio de hoteles no puede ser nulo");
        }
        if (propietarioRepositorio == null) {
            throw new IllegalArgumentException(
                    "El repositorio de propietarios no puede ser nulo");
        }
        if (cadenaHoteleraRepositorio == null) {
            throw new IllegalArgumentException(
                    "El repositorio de cadenas hoteleras no puede ser nulo");
        }
        if (hotelMapeador == null) {
            throw new IllegalArgumentException(
                    "El mapeador de hoteles no puede ser nulo");
        }
        this.hotelRepositorio = hotelRepositorio;
        this.propietarioRepositorio = propietarioRepositorio;
        this.cadenaHoteleraRepositorio = cadenaHoteleraRepositorio;
        this.hotelMapeador = hotelMapeador;
    }

    public HotelDto registrar(HotelCrearDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Los datos del hotel son requeridos");
        }
        Propietario propietario = buscarPropietarioActivo(request.idPropietario());
        CadenaHotelera cadenaHotelera = buscarCadenaActiva(request.idCadenaHotelera());

        Hotel hotel = new Hotel(
                request.nombreHotel(),
                propietario,
                cadenaHotelera,
                request.celularHotel()
        );
        hotel = hotelRepositorio.guardar(hotel);
        return hidratarYMapearUnico(hotel, propietario, cadenaHotelera);
    }

    public HotelDto actualizar(HotelActualizarDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Los datos del hotel son requeridos");
        }
        Hotel hotel = ReglasValidacion.validarIdExiste(
                hotelRepositorio.buscarPorId(request.idHotel()),
                request.idHotel()
        );
        boolean cambioNombre = request.nuevoNombre() != null;
        boolean cambioCelular = request.nuevoCelular() != null;
        boolean cambioPropietario = request.nuevoIdPropietario() != null;
        boolean cambioCadena = request.nuevoIdCadenaHotelera() != null;

        Propietario nuevoPropietario = null;
        if (cambioPropietario) {
            nuevoPropietario = buscarPropietarioActivo(request.nuevoIdPropietario());
        }
        CadenaHotelera nuevaCadena = null;
        if (cambioCadena) {
            nuevaCadena = buscarCadenaActiva(request.nuevoIdCadenaHotelera());
        }

        if (cambioNombre) {
            hotel.actualizarNombre(request.nuevoNombre());
        }
        if (cambioCelular) {
            hotel.actualizarCelular(request.nuevoCelular());
        }
        if (cambioPropietario) {
            hotel.cambiarPropietario(nuevoPropietario);
        }
        if (cambioCadena) {
            hotel.cambiarCadenaHotelera(nuevaCadena);
        }
        hotel = hotelRepositorio.actualizar(hotel);
        return hidratarYMapearUnico(hotel);
    }

    public HotelDto suspender(UUID idHotel) {
        Hotel hotel = ReglasValidacion.validarIdExiste(
                hotelRepositorio.buscarPorId(idHotel),
                idHotel
        );
        ReglasValidacion.validarPuedeSuspender(hotel.getEstadoHotel());
        hotel.suspender();
        hotel = hotelRepositorio.actualizar(hotel);
        return hidratarYMapearUnico(hotel);
    }

    public HotelDto reactivar(UUID idHotel) {
        Hotel hotel = ReglasValidacion.validarIdExiste(
                hotelRepositorio.buscarPorId(idHotel),
                idHotel
        );
        ReglasValidacion.validarPuedeReactivar(hotel.getEstadoHotel());
        // Para volver a utilizarse, sus relaciones deben seguir activas
        buscarPropietarioActivo(hotel.getIdPropietario());
        buscarCadenaActiva(hotel.getIdCadenaHotelera());
        hotel.reactivar();
        hotel = hotelRepositorio.actualizar(hotel);
        return hidratarYMapearUnico(hotel);
    }

    public List<HotelDto> listarTodos() {
        List<Hotel> hoteles = hotelRepositorio.listarTodos();
        if (hoteles.isEmpty()) {
            return List.of();
        }
        Set<UUID> idsPropietarios = new HashSet<>();
        Set<UUID> idsCadenas = new HashSet<>();
        for (Hotel hotel : hoteles) {
            idsPropietarios.add(hotel.getIdPropietario());
            idsCadenas.add(hotel.getIdCadenaHotelera());
        }
        PropietarioCargador propietarioCargador = new PropietarioCargador(propietarioRepositorio);
        propietarioCargador.cargarPorIds(new ArrayList<>(idsPropietarios));
        CadenaHoteleraCargador cadenaCargador = new CadenaHoteleraCargador(cadenaHoteleraRepositorio);
        cadenaCargador.cargarPorIds(new ArrayList<>(idsCadenas));
        return hotelMapeador.toDtoList(hoteles, propietarioCargador, cadenaCargador);
    }

    public HotelDto buscarPorId(UUID idHotel) {
        Hotel hotel = ReglasValidacion.validarIdExiste(
                hotelRepositorio.buscarPorId(idHotel),
                idHotel
        );
        return hidratarYMapearUnico(hotel);
    }

    private Propietario buscarPropietarioActivo(UUID idPropietario) {
        Propietario propietario = ReglasValidacion.validarRelacionExiste(
                propietarioRepositorio.buscarPorId(idPropietario),
                "Propietario"
        );
        ReglasValidacion.validarRelacionActiva(
                propietario.getEstadoPropietario(),
                "Propietario"
        );
        return propietario;
    }

    private CadenaHotelera buscarCadenaActiva(UUID idCadenaHotelera) {
        CadenaHotelera cadenaHotelera = ReglasValidacion.validarRelacionExiste(
                cadenaHoteleraRepositorio.buscarPorId(idCadenaHotelera),
                "Cadena hotelera"
        );
        ReglasValidacion.validarRelacionActiva(
                cadenaHotelera.getEstadoCadenaHotelera(),
                "Cadena hotelera"
        );
        return cadenaHotelera;
    }

    private HotelDto hidratarYMapearUnico(Hotel hotel) {
        Propietario propietario = propietarioRepositorio.buscarPorId(hotel.getIdPropietario())
                .orElseThrow(() -> new IllegalStateException(
                        "Error de integridad: Propietario no encontrado con ID: " + hotel.getIdPropietario()));
        CadenaHotelera cadenaHotelera = cadenaHoteleraRepositorio.buscarPorId(hotel.getIdCadenaHotelera())
                .orElseThrow(() -> new IllegalStateException(
                        "Error de integridad: Cadena hotelera no encontrada con ID: " + hotel.getIdCadenaHotelera()));
        return hidratarYMapearUnico(hotel, propietario, cadenaHotelera);
    }

    private HotelDto hidratarYMapearUnico(Hotel hotel, Propietario propietario, CadenaHotelera cadenaHotelera) {
        PropietarioCargador propietarioCargador = new PropietarioCargador(propietarioRepositorio);
        propietarioCargador.registrarEnCache(propietario);
        CadenaHoteleraCargador cadenaCargador = new CadenaHoteleraCargador(cadenaHoteleraRepositorio);
        cadenaCargador.registrarEnCache(cadenaHotelera);
        return hotelMapeador.toDto(hotel, propietarioCargador, cadenaCargador);
    }

    public int contarHoteles() {
        return (int) hotelRepositorio.contar();
    }
}
