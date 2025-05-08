package es.uma.aedo.data;

import java.util.HashMap;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

@Entity
public class PreguntaTipo4 extends Pregunta{
    //------------Atributos------------
    @ElementCollection   
    private HashMap<String, Integer> respuestas;

    //------------Constructor------------
    public PreguntaTipo4() {}

    //------------Getters y Setters------------
    public HashMap<String, Integer> getRespuestas() { return respuestas; }
    public void setRespuestas(HashMap<String, Integer> respuestas) { this.respuestas = respuestas; }
}
