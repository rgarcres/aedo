package es.uma.aedo.data.entidades;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Bloque extends AbstractEntity{
    //------------Atributos------------
    private String nombre;
    private String descripcion;
    @OneToMany(mappedBy = "bloque")
    private List<Pregunta> preguntas;
    @ManyToOne
    @JoinColumn(name="camp_id")
    private Campanya camp;

    //------------Constructor------------
    public Bloque() {}

    //------------Getters y Setters------------
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public List<Pregunta> getPreguntas() { return preguntas; }
    public void setPreguntas(List<Pregunta> preguntas) { this.preguntas = preguntas; }    

    //------------Métodos------------
    @Override
    public String toString(){
        return nombre + ": " + descripcion;
    }
}
