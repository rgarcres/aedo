package es.uma.aedo.data.entidades;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Region extends AbstractEntity {
    //------------Atributos------------
    @Column(name="localidad",nullable=false)
    private String localidad;
    @Column(name="provincia",nullable=false)
    private String provincia;
    @Column(name="comunidad_autonoma",nullable=false)
    private String comunidadAutonoma;
    @OneToMany(mappedBy = "region")
    private List<Usuario> usuarios;

    //------------Constructor------------
    public Region() {}
    public Region(String ID){
        this.setId(ID);
    }

    //------------Getters y Setters------------
    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }
    
    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }
    
    public String getComunidadAutonoma() { return comunidadAutonoma; }
    public void setComunidadAutonoma(String comunidadAutonoma) { this.comunidadAutonoma = comunidadAutonoma; }
    
    public List<Usuario> getUsuarios(){ return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }

    //------------Métodos------------
    @Override
    public String toString(){
         return localidad + '-' + provincia + '-' + comunidadAutonoma;
    }
}
