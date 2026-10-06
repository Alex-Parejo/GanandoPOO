package edu.unimag.modelo;

import com.cleandev.tpa.api.annotations.TpaConvert;
import com.cleandev.tpa.api.annotations.TpaId;
import edu.unimag.modelo.convertidor.EstadoEntidadConverter;
import edu.unimag.modelo.enumeracion.EstadoEntidad;
import java.util.UUID;


public class CadenaHotelera {
    @TpaId
    private UUID idCadenaHotelera;
    private String nombreCadenaHotelera;
    private String paisOrigenCadenaHotelera;
    private Integer numeroHotelesCadenaHotelera;
    @TpaConvert(converter = EstadoEntidadConverter.class)
    private EstadoEntidad estadoCadenaHotelera;
    
    //reflexion

    protected CadenaHotelera() {
    }
     
    //Creacion

    public CadenaHotelera(String nombreCadenaHotelera, String paisOrigenCadenaHotelera, Integer numeroHotelesCadenaHotelera) {
        this.nombreCadenaHotelera = nombreCadenaHotelera;
        this.paisOrigenCadenaHotelera = paisOrigenCadenaHotelera;
        this.numeroHotelesCadenaHotelera = numeroHotelesCadenaHotelera;
        this.estadoCadenaHotelera = EstadoEntidad.ACTIVO;
    }

    //Hidratacion
    public CadenaHotelera(UUID idCadenaHotelera, String nombreCadenaHotelera, String paisOrigenCadenaHotelera, Integer numeroHotelesCadenaHotelera, EstadoEntidad estadoCadenaHotelera) {
        this.nombreCadenaHotelera = nombreCadenaHotelera;
        this.paisOrigenCadenaHotelera = paisOrigenCadenaHotelera;
        this.numeroHotelesCadenaHotelera = numeroHotelesCadenaHotelera;
        this.estadoCadenaHotelera = estadoCadenaHotelera;
        if (idCadenaHotelera == null) {
            throw new IllegalArgumentException("El ID de la cadena hotelera es obligatorio en hidratacion");
        }
        this.idCadenaHotelera = idCadenaHotelera;
    }
    
    //Metodos de comportamiento
    public void actualizarNombre(String nuevoNombre) {
        if (nuevoNombre.equalsIgnoreCase(this.nombreCadenaHotelera)) {
            throw new IllegalArgumentException("El nuevo nombre es igual al actual");
        }
        this.nombreCadenaHotelera = nuevoNombre;
    }
    
    public void actualizarPaisOrigen(String nuevoPais) {
        if (nuevoPais.equalsIgnoreCase(this.paisOrigenCadenaHotelera)) {
            throw new IllegalArgumentException("El nuevo Pais es igual al actual");
        }
        this.paisOrigenCadenaHotelera = nuevoPais;
    }
    
    public void actualizarNumeroHoteles(Integer nuevoNumero) {
        if (nuevoNumero.equals(this.numeroHotelesCadenaHotelera)) {
            throw new IllegalArgumentException("El nuevo numero de hoteles es igual al actual");
        }
        this.numeroHotelesCadenaHotelera = nuevoNumero;
    }

    public void suspender() {
        this.estadoCadenaHotelera = estadoCadenaHotelera.cambiarEstadoA(EstadoEntidad.INACTIVO);
    }

    public void reactivar() {
        this.estadoCadenaHotelera = estadoCadenaHotelera.cambiarEstadoA(EstadoEntidad.ACTIVO);
    }

    public boolean estaActivo() {
        return this.estadoCadenaHotelera == EstadoEntidad.ACTIVO;
    }
     
    //Getters

    public UUID getIdCadenaHotelera() {
        return idCadenaHotelera;
    }

    public String getNombreCadenaHotelera() {
        return nombreCadenaHotelera;
    }

    public String getPaisOrigenCadenaHotelera() {
        return paisOrigenCadenaHotelera;
    }

    public Integer getNumeroHotelesCadenaHotelera() {
        return numeroHotelesCadenaHotelera;
    }

    public EstadoEntidad getEstadoCadenaHotelera() {
        return estadoCadenaHotelera;
    }

}
