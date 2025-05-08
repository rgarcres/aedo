package es.uma.aedo.data;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Grupo {
    //------------Atributos------------
    @Id
    private String grupoID;
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
    //ID
    public String getID() { return grupoID; }
    public void setID(String ID) { this.grupoID = ID; }

    //Nombre
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    //Descripcion
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    //Usuarios
    public List<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }

    //------------Métodos------------
    @Override
    public String toString(){
        return nombre;
    }

    @Override
    public int hashCode(){
        return Objects.hash(grupoID);
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Grupo g)) return false;

        return grupoID.equals(g.getID());
    }
}
