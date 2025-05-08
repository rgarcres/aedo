package es.uma.aedo.data;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Pregunta {
    //------------Atributos------------   
    @Id
    private String preguntaID;
    private String enunciado;
    @ManyToOne
    @JoinColumn(name="bloqueID")
    private Bloque bloque;
    //------------Constructor------------
    public Pregunta(){}

    //------------Getters y Setters------------
    public String getID() { return preguntaID; }
    public void setID(String ID) { this.preguntaID = ID; }

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }

    //------------Métodos------------
    @Override
    public String toString(){
        return enunciado;
    }

    @Override
    public int hashCode(){
        return Objects.hash(preguntaID);
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Pregunta p)) return false;

        return preguntaID.equals(p.getID());
    }
}
