package es.uma.aedo.data.entidades;
import java.util.List;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Grupo extends AbstractEntity{
    //------------Atributos------------
    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name="descripcion",nullable = true)
    private String descripcion;
    @OneToMany(mappedBy="grupo")
    private List<Usuario> usuarios;

    //------------Constructor------------
    public Grupo(){
    }

    //------------Getters y Setters------------
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }

    //------------Métodos------------
    @Override
    public String toString(){
        return nombre;
    }
}
