package es.uma.aedo.data.entidades;

import java.util.ArrayList;
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
        this.posiblesRespuestas = new ArrayList<>();
    }

    //------------Getters y Setters------------
    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }
    
    public List<String> getPosiblesRespuestas() { return posiblesRespuestas; }
    public void setPosiblesRespuestas(List<String> posiblesRespuestas) { this.posiblesRespuestas = posiblesRespuestas; }
    public void addRespuesta(String res){
        posiblesRespuestas.add(res);
    }
    public void removeRespuesta(String res){
        posiblesRespuestas.remove(res);
    }

    
    public String getRespuestaCorrecta() { return respuestaCorrecta; }
    public void setRespuestaCorrecta(String respuestaCorrecta) { this.respuestaCorrecta = respuestaCorrecta; }

    //------------Métodos------------
    @Override
    public String toString(){
        return enunciado;
    }
}
