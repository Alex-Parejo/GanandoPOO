package edu.unimag.modelo;

import com.cleandev.tpa.api.annotations.TpaConvert;
import com.cleandev.tpa.api.annotations.TpaId;
import edu.unimag.modelo.convertidor.EstadoEntidadConverter;
import edu.unimag.modelo.enumeracion.EstadoEntidad;
import java.util.UUID;


public class Propietario {
    @TpaId
    private UUID idPropietario;
    private String nombrePropietario;
    @TpaConvert(converter = EstadoEntidadConverter.class)
    private EstadoEntidad estadoPropietario;
    private String documentoPropietario;
    private String celularPropietario;
    //Reflexion

    protected Propietario() {
    }
    
    //Creacion

    public Propietario(String nombrePropietario, String documentoPropietario, String celularPropietario) {
        this.nombrePropietario = nombrePropietario;
        this.documentoPropietario = documentoPropietario;
        this.celularPropietario = celularPropietario;
        this.estadoPropietario = EstadoEntidad.ACTIVO;
    }

    //Hidratacion
    public Propietario(UUID idPropietario, String nombrePropietario, EstadoEntidad estadoPropietario, String documentoPropietario, String celularPropietario) {

        this.nombrePropietario = nombrePropietario;
        this.estadoPropietario = estadoPropietario;
        this.documentoPropietario = documentoPropietario;
        this.celularPropietario = celularPropietario;
        if (idPropietario==null) {
            throw new IllegalArgumentException("El ID del propietario es obligatorio en hidratacion");
            
        }
        this.idPropietario = idPropietario;
        
        
    }
    
    //Metodos de comportamiento
    public void actualizarNombre(String nuevoNombre) {
        if (nuevoNombre.equalsIgnoreCase(this.nombrePropietario)) {
            throw new IllegalArgumentException("El nuevo nombre es igual al actual");
        }
        this.nombrePropietario = nuevoNombre;
    }
    
    public void actualizarCelular(String nuevoCelular) {
        if (nuevoCelular.equalsIgnoreCase(this.celularPropietario)) {
            throw new IllegalArgumentException("El numero de celular es igual al actual");
        }
        this.celularPropietario = nuevoCelular;
    }

    public void suspender() {
        this.estadoPropietario = estadoPropietario.cambiarEstadoA(EstadoEntidad.INACTIVO);
    } 
       
    public void reactivar() {
        this.estadoPropietario = estadoPropietario.cambiarEstadoA(EstadoEntidad.ACTIVO);
    }   
       
    public boolean estaActivo() {
        return this.estadoPropietario == EstadoEntidad.ACTIVO;
    }
        
    //Getters

    public UUID getIdPropietario() {
        return idPropietario;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public EstadoEntidad getEstadoPropietario() {
        return estadoPropietario;
    }

    public String getDocumentoPropietario() {
        return documentoPropietario;
    }

    public String getCelularPropietario() {
        return celularPropietario;
    }

}
