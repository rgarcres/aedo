package es.uma.aedo.data.entidades;

import java.util.List;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("3")
public class RespuestaTipo3 extends Respuesta{
    //------------Atributos------------   
    @ElementCollection
    private List<String> seleccionadas;
    
    //------------Constructor------------
    public RespuestaTipo3() {}
    
    public List<String> getseleccionadas() { return seleccionadas; }
    public void setseleccionadas(List<String> seleccionadas) { this.seleccionadas = seleccionadas; } 
}
