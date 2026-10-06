package edu.unimag.repositorio.cargador;

import edu.unimag.modelo.Hotel;
import edu.unimag.repositorio.HotelRepositorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class HotelCargador
        implements CargadorDatos<Hotel, UUID> {

    private final HotelRepositorio repositorio;
    private final Map<UUID, Hotel> cache;

    public HotelCargador(HotelRepositorio repositorio) {

        if (repositorio == null) {
            throw new IllegalArgumentException(
                    "El repositorio de hoteles no puede ser nulo");
        }

        this.repositorio = repositorio;
        this.cache = new HashMap<>();
    }

    @Override
    public void cargarTodos() {

        this.cache.clear();

        for (Hotel hotel : repositorio.listarTodos()) {

            if (hotel != null && hotel.getIdHotel() != null) {
                cache.put(hotel.getIdHotel(), hotel);
            }
        }
    }

    @Override
    public void cargarPorIds(List<UUID> ids) {

        if (ids == null || ids.isEmpty()) {
            return;
        }

        List<UUID> idsFaltantes = new ArrayList<>();

        for (UUID id : ids) {

            if (id != null && !cache.containsKey(id)) {
                idsFaltantes.add(id);
            }
        }

        if (idsFaltantes.isEmpty()) {
            return;
        }

        Set<UUID> idsFaltantesSet =
                new HashSet<>(idsFaltantes);

        for (Hotel hotel : repositorio.listarTodos()) {

            if (hotel != null
                    && hotel.getIdHotel() != null
                    && idsFaltantesSet.contains(
                            hotel.getIdHotel())) {

                cache.put(
                        hotel.getIdHotel(),
                        hotel
                );
            }
        }
    }

    @Override
    public void registrarEnCache(Hotel entidad) {

        if (entidad != null
                && entidad.getIdHotel() != null) {

            cache.put(
                    entidad.getIdHotel(),
                    entidad
            );
        }
    }

    @Override
    public Optional<Hotel> obtener(UUID id) {

        if (id == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public boolean existe(UUID id) {

        if (id == null) {
            return false;
        }

        return cache.containsKey(id);
    }
}