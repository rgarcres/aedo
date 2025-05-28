package es.uma.aedo.data.entidades;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Bloque extends AbstractEntity{
    //------------Atributos------------
    private String nombre;
    private String descripcion;
    @OneToMany(mappedBy = "bloque")
    private List<Pregunta> preguntas;

    //------------Constructor------------
    public Bloque() {}
    public Bloque(Bloque bloque){
        this.nombre = bloque.getNombre();
        this.descripcion = bloque.getDescripcion();
        this.preguntas = new ArrayList<>(bloque.getPreguntas());
    }

    //------------Getters y Setters------------
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public List<Pregunta> getPreguntas() { return new ArrayList<>(preguntas); }
    public void setPreguntas(List<Pregunta> preguntas) { this.preguntas = new ArrayList<>(preguntas); }    

    //------------Métodos------------
    @Override
    public String toString(){
        return nombre + ": " + descripcion;
    }
}
