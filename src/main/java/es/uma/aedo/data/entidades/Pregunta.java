package es.uma.aedo.data.entidades;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Pregunta extends AbstractEntity {
    //------------Atributos------------   
    private String enunciado;
    private Integer tipo;
    @ManyToOne
    @JoinColumn(name="bloque_id")
    private Bloque bloque;
    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Respuesta> respuestas;

    //------------Constructor------------
    public Pregunta(){}

    //------------Getters y Setters------------
    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }

    public Integer getTipo() { return tipo; }
    public void setTipo(Integer tipo) { this.tipo = tipo; }

    public Bloque getBloque() { return bloque; }
    public void setBloque(Bloque bloque) { this.bloque = bloque; }

    public List<Respuesta> getRespuestas() { return respuestas; }
    public void setRespuestas(List<Respuesta> respuestas) { this.respuestas = respuestas; }

    //------------Métodos------------
    @Override
    public String toString(){
        return enunciado;
    }
}
