package es.uma.aedo.data.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class BloqueProgramado extends AbstractEntity{
    //------------Atributos------------
    @ManyToOne()
    @JoinColumn(name = "camp_id")
    private Campanya camp;
    @ManyToOne()
    @JoinColumn(name = "bloque_id")
    private Bloque bloque;
    private LocalDateTime fechaHora;

    //------------Constructor------------
    public BloqueProgramado() {}
    public BloqueProgramado(BloqueProgramado bp){
        this.camp = new Campanya(bp.getCamp());
        this.bloque = new Bloque(bp.getBloque());
        this.fechaHora = bp.getFechaHora();
    }
    
    //------------Getters y Setters------------
    public Campanya getCamp() { return camp; }
    public void setCamp(Campanya camp) { this.camp = camp; }

    public Bloque getBloque() { return bloque; }
    public void setBloque(Bloque bloque) { this.bloque = bloque; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    //------------Métodos------------
    @Override
    public String toString(){
        return bloque.getNombre();
    }
}
