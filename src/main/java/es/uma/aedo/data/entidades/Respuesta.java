package es.uma.aedo.data.entidades;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.INTEGER)
public abstract class Respuesta extends AbstractEntity{
    //------------Atributos------------ 
    @ManyToOne
    @JoinColumn(name="usuario_id")
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name="pregunta_id")
    private Pregunta pregunta;

    //------------Constructor------------ 
    public Respuesta() {}

    //------------Getters y Setters------------
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public Pregunta getPregunta() { return pregunta; }
    public void setPregunta(Pregunta pregunta) {this.pregunta = pregunta; }
}
