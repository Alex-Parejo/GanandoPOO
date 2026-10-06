package edu.unimag.modelo;

import com.cleandev.tpa.api.annotations.TpaConvert;
import com.cleandev.tpa.api.annotations.TpaId;
import edu.unimag.modelo.convertidor.EstadoEntidadConverter;
import edu.unimag.modelo.enumeracion.EstadoEntidad;
import java.util.UUID;

public class Hotel {

    @TpaId
    private UUID idHotel;
    private String nombreHotel;
    private Propietario propietario;
    private CadenaHotelera cadenaHotelera;
    private String celularHotel;
    @TpaConvert(converter = EstadoEntidadConverter.class)
    private EstadoEntidad estadoHotel;

    //reflexion
    public Hotel() {
    }

    //creacion
    public Hotel(String nombreHotel, Propietario propietario, CadenaHotelera cadenaHotelera, String celularHotel) {
        this.nombreHotel = nombreHotel;
        this.propietario = propietario;
        this.cadenaHotelera = cadenaHotelera;
        this.celularHotel = celularHotel;
        this.estadoHotel = EstadoEntidad.ACTIVO;
    }

    //hidratacion
    public Hotel(UUID idHotel, String nombreHotel, Propietario propietario, CadenaHotelera cadenaHotelera, String celularHotel, EstadoEntidad estadoHotel) {
        this.nombreHotel = nombreHotel;
        this.propietario = propietario;
        this.cadenaHotelera = cadenaHotelera;
        this.celularHotel = celularHotel;
        this.estadoHotel = estadoHotel;
        if (idHotel == null) {
            throw new IllegalArgumentException("No se encuentra id");
        }
        this.idHotel = idHotel;
    }

    //Metodos de comportamiento
    public void actualizarNombre(String nuevoNombre) {
        if (nuevoNombre.trim().equalsIgnoreCase(nombreHotel)) {
            throw new IllegalArgumentException("El nombre es igual al actual");
        }
        nombreHotel = nuevoNombre;
    }

    public void actualizarCelular(String nuevoCelular) {
        if (nuevoCelular.equals(this.celularHotel)) {
            throw new IllegalArgumentException("El nuevo celular es igual al actual");
        }
        this.celularHotel = nuevoCelular;
    }

    public void cambiarPropietario(Propietario nuevoPropietario) {
        if (nuevoPropietario == null) {
            throw new IllegalArgumentException("El nuevo propietario es obligatorio");
        }
        if (nuevoPropietario.getIdPropietario().equals(getIdPropietario())) {
            throw new IllegalArgumentException("El nuevo Propietario es igual al actual");
        }
        this.propietario = nuevoPropietario;
    }

    public void cambiarCadenaHotelera(CadenaHotelera nuevaCadenaHotelera) {
        if (nuevaCadenaHotelera == null) {
            throw new IllegalArgumentException("La nueva cadena hotelera es obligatoria");
        }
        if (nuevaCadenaHotelera.getIdCadenaHotelera().equals(getIdCadenaHotelera())) {
            throw new IllegalArgumentException("La nueva cadena hotelera es igual al actual");
        }
        this.cadenaHotelera = nuevaCadenaHotelera;
    }

    public void suspender() {
        this.estadoHotel = estadoHotel.cambiarEstadoA(EstadoEntidad.INACTIVO);
    }

    public void reactivar() {
        this.estadoHotel = estadoHotel.cambiarEstadoA(EstadoEntidad.ACTIVO);
    }

    public boolean estaActivo() {
        return this.estadoHotel == EstadoEntidad.ACTIVO;
    }

    //Getters
    public UUID getIdHotel() {
        return idHotel;
    }

    public String getNombreHotel() {
        return nombreHotel;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public CadenaHotelera getCadenaHotelera() {
        return cadenaHotelera;
    }

    public String getCelularHotel() {
        return celularHotel;
    }

    public EstadoEntidad getEstadoHotel() {
        return estadoHotel;
    }

    public UUID getIdPropietario() {
        return propietario.getIdPropietario();
    }

    public UUID getIdCadenaHotelera() {
        return cadenaHotelera.getIdCadenaHotelera();
    }

}
