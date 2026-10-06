package edu.unimag.repositorio.cargador;

import edu.unimag.modelo.CadenaHotelera;
import edu.unimag.repositorio.CadenaHoteleraRepositorio;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class CadenaHoteleraCargador implements CargadorDatos<CadenaHotelera, UUID> {

    private final Map<UUID, CadenaHotelera> cache;
    private final CadenaHoteleraRepositorio repositorio;

    public CadenaHoteleraCargador(CadenaHoteleraRepositorio repositorio) {
        if (repositorio == null) {
            throw new IllegalArgumentException("El repositorio no puede ser nulo");
        }
        this.repositorio = repositorio;
        this.cache = new HashMap<>();
    }

    @Override
    public void cargarTodos() {
        cache.clear();
        for (CadenaHotelera cadenaHotelera : repositorio.listarTodos()) {
            cache.put(cadenaHotelera.getIdCadenaHotelera(), cadenaHotelera);
        }
    }

    @Override
    public void cargarPorIds(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        List<UUID> faltantes = new ArrayList<>();
        for (UUID id : ids) {
            if (id != null && !cache.containsKey(id)) {
                faltantes.add(id);
            }
        }
        if (faltantes.isEmpty()) {
            return;
        }
        Set<UUID> idsFaltantes = new HashSet<>(faltantes);
        for (CadenaHotelera cadenaHotelera : repositorio.listarTodos()) {
            if (idsFaltantes.contains(cadenaHotelera.getIdCadenaHotelera())) {
                cache.put(cadenaHotelera.getIdCadenaHotelera(), cadenaHotelera);
            }
        }
    }

    @Override
    public void registrarEnCache(CadenaHotelera entidad) {
        if (entidad != null && entidad.getIdCadenaHotelera()!= null) {
            cache.put(entidad.getIdCadenaHotelera(), entidad);
        }
    }

    @Override
    public Optional<CadenaHotelera> obtener(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public boolean existe(UUID id) {
        return id != null && cache.containsKey(id);
    }
}
