package edu.unimag.mapeador;

import edu.unimag.dto.cadenahotelera.CadenaHoteleraDto;
import edu.unimag.dto.hotel.HotelDto;
import edu.unimag.dto.propietario.PropietarioDto;
import edu.unimag.modelo.CadenaHotelera;
import edu.unimag.modelo.Hotel;
import edu.unimag.modelo.Propietario;
import edu.unimag.repositorio.cargador.CadenaHoteleraCargador;
import edu.unimag.repositorio.cargador.PropietarioCargador;

import java.util.ArrayList;
import java.util.List;

public class HotelMapeador {

    private final PropietarioMapeador propietarioMapeador;
    private final CadenaHoteleraMapeador cadenaHoteleraMapeador;

    public HotelMapeador(
            PropietarioMapeador propietarioMapeador,
            CadenaHoteleraMapeador cadenaHoteleraMapeador) {

        if (propietarioMapeador == null) {
            throw new IllegalArgumentException(
                    "El mapeador de propietarios no puede ser nulo");
        }

        if (cadenaHoteleraMapeador == null) {
            throw new IllegalArgumentException(
                    "El mapeador de cadenas hoteleras no puede ser nulo");
        }

        this.propietarioMapeador = propietarioMapeador;
        this.cadenaHoteleraMapeador = cadenaHoteleraMapeador;
    }

    public List<HotelDto> toDtoList(
            List<Hotel> entidades,
            PropietarioCargador propietarioCargador,
            CadenaHoteleraCargador cadenaHoteleraCargador) {

        if (entidades == null || entidades.isEmpty()) {
            return List.of();
        }

        if (propietarioCargador == null) {
            throw new IllegalArgumentException(
                    "El cargador de propietarios no puede ser nulo");
        }

        if (cadenaHoteleraCargador == null) {
            throw new IllegalArgumentException(
                    "El cargador de cadenas hoteleras no puede ser nulo");
        }

        List<HotelDto> resultado =
                new ArrayList<>(entidades.size());

        for (Hotel entity : entidades) {

            if (!validarIntegridad(
                    entity,
                    propietarioCargador,
                    cadenaHoteleraCargador)) {

                continue;
            }

            resultado.add(
                    transformarADto(
                            entity,
                            propietarioCargador,
                            cadenaHoteleraCargador
                    )
            );
        }

        return resultado;
    }

    public HotelDto toDto(
            Hotel entity,
            PropietarioCargador propietarioCargador,
            CadenaHoteleraCargador cadenaHoteleraCargador) {

        if (entity == null) {
            throw new IllegalArgumentException(
                    "La entidad Hotel no puede ser nula");
        }

        if (propietarioCargador == null) {
            throw new IllegalArgumentException(
                    "El cargador de propietarios no puede ser nulo");
        }

        if (cadenaHoteleraCargador == null) {
            throw new IllegalArgumentException(
                    "El cargador de cadenas hoteleras no puede ser nulo");
        }

        if (!validarIntegridad(
                entity,
                propietarioCargador,
                cadenaHoteleraCargador)) {

            throw new IllegalStateException(
                    "Error de integridad en Hotel ID: "
                    + entity.getIdHotel()
            );
        }

        return transformarADto(
                entity,
                propietarioCargador,
                cadenaHoteleraCargador
        );
    }

    private boolean validarIntegridad(
            Hotel entity,
            PropietarioCargador propietarioCargador,
            CadenaHoteleraCargador cadenaHoteleraCargador) {

        if (entity == null) {
            return false;
        }

        if (entity.getPropietario() == null
                || entity.getCadenaHotelera() == null) {

            return false;
        }

        return propietarioCargador.existe(
                    entity.getPropietario().getIdPropietario())
                && cadenaHoteleraCargador.existe(
                    entity.getCadenaHotelera()
                            .getIdCadenaHotelera());
    }

    private HotelDto transformarADto(
            Hotel entity,
            PropietarioCargador propietarioCargador,
            CadenaHoteleraCargador cadenaHoteleraCargador) {

        Propietario propietario =
                propietarioCargador
                        .obtener(
                                entity.getPropietario()
                                        .getIdPropietario())
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Error de integridad: Propietario no encontrado para ID: "
                                        + entity.getPropietario()
                                                .getIdPropietario()
                                ));

        CadenaHotelera cadenaHotelera =
                cadenaHoteleraCargador
                        .obtener(
                                entity.getCadenaHotelera()
                                        .getIdCadenaHotelera())
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Error de integridad: Cadena hotelera no encontrada para ID: "
                                        + entity.getCadenaHotelera()
                                                .getIdCadenaHotelera()
                                ));

        PropietarioDto propietarioDto =
                propietarioMapeador.toDto(propietario);

        CadenaHoteleraDto cadenaHoteleraDto =
                cadenaHoteleraMapeador.toDto(cadenaHotelera);

        return new HotelDto(
                entity.getIdHotel(),
                entity.getNombreHotel(),
                propietarioDto,
                cadenaHoteleraDto,
                entity.getCelularHotel(),
                entity.getEstadoHotel()
        );
    }
}