package edu.unimag.dto.validacion;

import edu.unimag.modelo.enumeracion.EstadoEntidad;
import java.util.Optional;
import java.util.UUID;

public final class ReglasValidacion {

    private ReglasValidacion() {
    }
//Reglas de validacion claves para el contexto del ejercicio
    

    // 1. CONSTANTES DE LONGITUD Y FORMATO
    //    Limites usados por las validaciones de texto, documento y celular
    public static final int NOMBRE_MIN = 3;
    public static final int NOMBRE_MAX = 100;
    public static final int PAIS_MIN = 2;
    public static final int PAIS_MAX = 60;
    public static final int DOCUMENTO_MIN = 6;
    public static final int DOCUMENTO_MAX = 12;
    public static final int CELULAR_MIN = 7;
    public static final int CELULAR_MAX = 10;


    // 2. CAMPOS OBLIGATORIOS
    //    Un campo obligatorio no puede ser null ni estar vacio

    public static String limpiarRequerido(String valor, String mensajeError) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensajeError);
        }
        return valor.trim();
    }

    // 3. LONGITUD DE TEXTO
    //    Los campos de texto deben respetar una longitud minima y maxima

    public static String limpiarTextoConLongitud(
            String valor, int min, int max, String nombreCampo) {
        String limpio = limpiarRequerido(valor, nombreCampo + " es obligatorio");
        if (limpio.length() < min || limpio.length() > max) {
            throw new IllegalArgumentException(
                    nombreCampo + " debe tener entre " + min + " y " + max + " caracteres");
        }
        return limpio;
    }


    // 4. CAMPOS NUMERICOS
    //    Valores validos y cantidades que no pueden ser negativas
    
    public static Integer limpiarEnteroRequerido(Integer valor, String mensajeError) {
        if (valor == null) {
            throw new IllegalArgumentException(mensajeError);
        }
        return valor;
    }

    public static Integer limpiarEnteroPositivo(Integer valor, String mensajeError) {
        if (valor == null || valor <= 0) {
            throw new IllegalArgumentException(mensajeError);
        }
        return valor;
    }

    public static Integer limpiarEnteroNoNegativo(Integer valor, String mensajeError) {
        if (valor == null || valor < 0) {
            throw new IllegalArgumentException(mensajeError);
        }
        return valor;
    }


    // 5. FORMATOS
    //    Documento y celular: solo numeros y longitud valida.

    
    public static String limpiarDocumento(String documento) {
        String limpio = limpiarRequerido(documento, "El documento es obligatorio");
        if (!limpio.matches("\\d+")) {
            throw new IllegalArgumentException("El documento debe contener solo numeros");
        }
        if (limpio.length() < DOCUMENTO_MIN || limpio.length() > DOCUMENTO_MAX) {
            throw new IllegalArgumentException(
                    "El documento debe tener entre " + DOCUMENTO_MIN
                    + " y " + DOCUMENTO_MAX + " digitos");
        }
        return limpio;
    }

    public static String limpiarCelular(String celular) {
        String limpio = limpiarRequerido(celular, "El celular es obligatorio");
        if (!limpio.matches("\\d+")) {
            throw new IllegalArgumentException("El celular debe contener solo numeros");
        }
        if (limpio.length() < CELULAR_MIN || limpio.length() > CELULAR_MAX) {
            throw new IllegalArgumentException(
                    "El celular debe tener entre " + CELULAR_MIN
                    + " y " + CELULAR_MAX + " digitos");
        }
        return limpio;
    }


    // 6. IDS (UUID)
    //    - El id no puede ser null
    //    - Al crear, el id no puede existir ya
    //    - Al actualizar, el id debe corresponder a un registro existente
    //    Como los IDs son UUID no aplica la regla de IDs negativos

    public static UUID limpiarUuidRequerido(UUID valor, String mensajeError) {
        if (valor == null) {
            throw new IllegalArgumentException(mensajeError);
        }
        return valor;
    }

    public static <T> void validarIdNoExiste(Optional<T> encontrado, UUID id) {
        if (encontrado.isPresent()) {
            throw new IllegalArgumentException("Ya existe un registro con ID: " + id);
        }
    }

    public static <T> T validarIdExiste(Optional<T> encontrado, UUID id) {
        return encontrado.orElseThrow(
                () -> new IllegalArgumentException("No existe un registro con ID: " + id));
    }

 
    // 7. UNICIDAD
    //    Documento del Propietario y nombre de la CadenaHotelera.
    //    Se usa al crear y al actualizar: en "yaExiste" va el resultado de
    //    buscar el valor en otro registro distinto al que se modifica.

    public static void validarUnico(boolean yaExiste, String mensajeError) {
        if (yaExiste) {
            throw new IllegalStateException(mensajeError);
        }
    }


    // 8. ESTADO
    //    - Toda entidad nueva inicia ACTIVO.
    //    - Una entidad INACTIVO no puede usarse en nuevas relaciones.

    public static void validarEstadoInicialActivo(EstadoEntidad estado) {
        if (estado != EstadoEntidad.ACTIVO) {
            throw new IllegalStateException("Toda entidad nueva debe iniciar como ACTIVO");
        }
    }

    public static void validarEstadoActivo(EstadoEntidad estado, String mensajeError) {
        if (estado != EstadoEntidad.ACTIVO) {
            throw new IllegalStateException(mensajeError);
        }
    }

    public static void validarEstadoInactivo(EstadoEntidad estado, String mensajeError) {
        if (estado != EstadoEntidad.INACTIVO) {
            throw new IllegalStateException(mensajeError);
        }
    }


    // 9. RELACIONES (HOTEL -> PROPIETARIO / CADENAHOTELERA)
    //    La relacion debe existir y estar ACTIVA antes de usarse.
    //    "nombreRelacion" es el texto para el mensaje: "Propietario" o
    

    public static <T> T validarRelacionExiste(Optional<T> relacion, String nombreRelacion) {
        return relacion.orElseThrow(() -> new IllegalArgumentException(
                "No se puede crear una relacion con " + nombreRelacion + " inexistente"));
    }

    public static void validarRelacionActiva(EstadoEntidad estado, String nombreRelacion) {
        if (estado != EstadoEntidad.ACTIVO) {
            throw new IllegalStateException(
                    nombreRelacion + " esta INACTIVO, no se puede usar en una relacion");
        }
    }

 
    // 10. OPERACIONES: SUSPENDER Y REACTIVAR
    //    - Suspender: la entidad debe estar ACTIVO.
    //    - Reactivar: la entidad debe estar INACTIVO.
    //    La existencia se valida antes con validarIdExiste.
    
    public static void validarPuedeSuspender(EstadoEntidad estadoActual) {
        validarEstadoActivo(estadoActual, "No se puede suspender: la entidad ya esta INACTIVO");
    }

    public static void validarPuedeReactivar(EstadoEntidad estadoActual) {
        validarEstadoInactivo(estadoActual, "No se puede reactivar: la entidad ya esta ACTIVO");
    }
}