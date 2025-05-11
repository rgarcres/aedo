package es.uma.aedo.data.entidades;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@DiscriminatorValue("2")
public class RespuestaTipo2 extends Respuesta{
    //------------Atributos------------
    public enum RespuestaSiNoIntensidad {
        SI,
        NO,
        NO_SE
    }
    @Enumerated(EnumType.STRING)
    private RespuestaSiNoIntensidad respuesta;
    private Integer intensidad;
    
    //------------Constructor------------  
    public RespuestaTipo2() {}

    //------------Getters y Setters------------  
    public RespuestaSiNoIntensidad getRespuesta() { return respuesta; }
    public void setRespuesta(RespuestaSiNoIntensidad respuesta) { this.respuesta = respuesta; }

    public Integer getIntensidad() { return intensidad; }
    public void setIntensidad(Integer intensidad) { this.intensidad = intensidad; }
}
