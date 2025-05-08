package es.uma.aedo.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Pregunta extends AbstractEntity {
    //------------Atributos------------   
    private String enunciado;
    @ManyToOne
    @JoinColumn(name="bloqueID")
    private Bloque bloque;
    //------------Constructor------------
    public Pregunta(){}

    //------------Getters y Setters------------

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }

    //------------Métodos------------
    @Override
    public String toString(){
        return enunciado;
    }
}
