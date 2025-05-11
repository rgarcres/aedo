package es.uma.aedo.data.entidades;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@DiscriminatorValue("1")
public class PreguntaTipo1 extends Pregunta{
    //------------Atributos------------   
    public enum RespuestaSiNo {
        SI,
        NO,
        OCASIONALMENTE
    }
    @Enumerated(EnumType.STRING)
    private RespuestaSiNo respuesta;

    //------------Constructor------------  
    public PreguntaTipo1(){}

    //------------Getters y Setters------------  
    public RespuestaSiNo getRespuesta() { return respuesta; }
    public void setRespuesta(RespuestaSiNo respuesta) { this.respuesta = respuesta; }
}
