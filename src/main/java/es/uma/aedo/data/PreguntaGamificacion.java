package es.uma.aedo.data;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class PreguntaGamificacion extends AbstractEntity {
    //------------Atributos------------
    @Column(name = "enunciado", nullable = false)
    private String enunciado;
    private List<String> posiblesRespuestas;
    private String respuestaCorrecta;

    //------------Constructor------------
    public PreguntaGamificacion(){
    }

    //------------Getters y Setters------------
    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }
    
    public List<String> getPosiblesRespuestas() { return posiblesRespuestas; }
    public void setPosiblesRespuestas(List<String> posiblesRespuestas) { this.posiblesRespuestas = posiblesRespuestas; }
    
    public String getRespuestaCorrecta() { return respuestaCorrecta; }
    public void setRespuestaCorrecta(String respuestaCorrecta) { this.respuestaCorrecta = respuestaCorrecta; }

    //------------Métodos------------
    @Override
    public String toString(){
        return enunciado;
    }
}
