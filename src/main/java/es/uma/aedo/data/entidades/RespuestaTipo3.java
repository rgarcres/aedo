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
    private List<String> opciones;
    @ElementCollection
    private List<String> seleccionadas;
    
    //------------Constructor------------
    public RespuestaTipo3() {}

    //------------Getters y Setters------------
    public List<String> getOpciones() { return seleccionadas; }
    public void setOpciones(List<String> opciones) { this.opciones = seleccionadas; }
    
    public List<String> getseleccionadas() { return seleccionadas; }
    public void setseleccionadas(List<String> seleccionadas) { this.seleccionadas = seleccionadas; } 
}
