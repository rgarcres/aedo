package es.uma.aedo.data.entidades;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.INTEGER)
public abstract class Pregunta extends AbstractEntity {
    //------------Atributos------------   
    private String enunciado;
    @ManyToOne
    @JoinColumn(name="bloque_id")
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
