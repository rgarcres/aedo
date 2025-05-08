package es.uma.aedo.data;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Bloque {
    //------------Atributos------------
    @Id
    private String bloqueID;
    private String nombre;
    private String descripcion;
    @OneToMany(mappedBy = "bloque")
    private List<Pregunta> preguntas;
    @ManyToOne
    @JoinColumn(name="campID")
    private Campanya camp;

    //------------Constructor------------
    public Bloque() {}

    //------------Getters y Setters------------
    public String getID() { return bloqueID; }
    public void setID(String bloqueID) { this.bloqueID = bloqueID; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public List<Pregunta> getPreguntas() { return preguntas; }
    public void setPreguntas(List<Pregunta> preguntas) { this.preguntas = preguntas; }    

    //------------Métodos------------
    @Override
    public String toString(){
        return nombre;
    }

    @Override
    public int hashCode(){
        return Objects.hash(bloqueID);
    }
    
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Bloque b)) return false;

        return bloqueID.equals(b.getID());
    }
}
