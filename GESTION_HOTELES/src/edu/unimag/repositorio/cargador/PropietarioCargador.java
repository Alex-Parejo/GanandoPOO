package edu.unimag.repositorio.cargador;

import edu.unimag.modelo.Propietario;
import edu.unimag.repositorio.PropietarioRepositorio;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class PropietarioCargador implements CargadorDatos<Propietario, UUID> {

    private final Map<UUID, Propietario> cache;
    private final PropietarioRepositorio repositorio;

    public PropietarioCargador(PropietarioRepositorio repositorio) {
        if (repositorio == null) {
            throw new IllegalArgumentException("El repositorio no puede ser nulo");
        }
        this.repositorio = repositorio;
        this.cache = new HashMap<>();
    }

    @Override
    public void cargarTodos() {
        cache.clear();
        for (Propietario propietario : repositorio.listarTodos()) {
            cache.put(propietario.getIdPropietario(), propietario);
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
        for (Propietario propietario : repositorio.listarTodos()) {
            if (idsFaltantes.contains(propietario.getIdPropietario())) {
                cache.put(propietario.getIdPropietario(), propietario);
            }
        }
    }

    @Override
    public void registrarEnCache(Propietario entidad) {
        if (entidad != null && entidad.getIdPropietario()!= null) {
            cache.put(entidad.getIdPropietario(), entidad);
        }
    }

    @Override
    public Optional<Propietario> obtener(UUID id) {
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
