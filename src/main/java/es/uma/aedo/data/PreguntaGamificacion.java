package es.uma.aedo.data;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class PreguntaGamificacion {
    //------------Atributos------------
    @Id
    private String preguntaGamID;
    @Column(name = "enunciado", nullable = false)
    private String enunciado;
    private List<String> posiblesRespuestas;
    private String respuestaCorrecta;

    //------------Constructor------------
    public PreguntaGamificacion(){
    }

    //------------Getters y Setters------------
    public String getID() {return preguntaGamID; }

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

    @Override
    public int hashCode(){
        return Objects.hash(preguntaGamID);
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof PreguntaGamificacion pg)) return false;
        
        return preguntaGamID.equals(pg.getID());
    }
}
