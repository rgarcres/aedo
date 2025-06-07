package es.uma.aedo.data.entidades;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

@Entity
public class Grupo extends AbstractEntity{
    //------------Atributos------------
    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name="descripcion",nullable = true)
    private String descripcion;
    @ManyToMany(mappedBy = "grupos")
    private List<Usuario> usuarios;
    @ManyToMany(mappedBy="grupos")
    private List<Campanya> camps;

    //------------Constructor------------
    public Grupo(){}
    public Grupo(Grupo g){
        this.nombre = g.getNombre();
        this.descripcion = g.getDescripcion();
        this.usuarios = new ArrayList<>(g.getUsuarios());
        this.camps = new ArrayList<>(g.getCampanyas());
    }

    //------------Getters y Setters------------
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }
    public void addUsuario(Usuario usuario){
        if(!usuarios.contains(usuario)){
            usuarios.add(usuario);
            usuario.getGrupos().add(this);
        }
    }
    public void removeUsuario(Usuario usuario) {
        if (usuarios.remove(usuario)) {
            usuario.getGrupos().remove(this);
        }
    }

    public List<Campanya> getCampanyas() { return camps; }
    public void setCampanyas(List<Campanya> camps) { this.camps = camps; }

    //------------Métodos------------
    @Override
    public String toString(){
        return nombre;
    }
}
