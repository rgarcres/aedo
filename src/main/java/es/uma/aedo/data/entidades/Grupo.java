package es.uma.aedo.data.entidades;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Grupo extends AbstractEntity{
    //------------Atributos------------
    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name="descripcion",nullable = true)
    private String descripcion;
    @ManyToMany
    @JoinTable(
        name = "grupo_usuario",
        joinColumns = @JoinColumn(name = "grupo_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> usuarios;
    @ManyToMany(mappedBy="grupos")
    private List<Campanya> camps;

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

    public List<Campanya> getCampanyas() { return camps; }
    public void setCampanyas(List<Campanya> camps) { this.camps = camps; }

    //------------Métodos------------
    @Override
    public String toString(){
        return nombre;
    }
}
