package es.uma.aedo.data.entidades;

import java.util.HashMap;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("4")
public class RespuestaTipo4 extends Respuesta{
    //------------Atributos------------
    @ElementCollection   
    private HashMap<String, Integer> respuestas;

    //------------Constructor------------
    public RespuestaTipo4() {}

    //------------Getters y Setters------------
    public HashMap<String, Integer> getRespuestas() { return respuestas; }
    public void setRespuestas(HashMap<String, Integer> respuestas) { this.respuestas = respuestas; }
}
