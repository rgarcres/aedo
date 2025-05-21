package es.uma.aedo.data.entidades;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Campanya extends AbstractEntity {
    //------------Atributos------------   
    private String nombre;
    @Column(name="fecha_inicio")
    private LocalDate inicio;
    @Column(name="fecha_fin")
    private LocalDate fin;
    private String objetivo;
    private String demografia;
    @ManyToMany()
    @JoinTable(name = "camp_grupo", 
    joinColumns = @JoinColumn(name="camp_fk"),
    inverseJoinColumns = @JoinColumn(name="grupo_fk"))
    private List<Grupo> grupos;
    @OneToMany(mappedBy = "camp")
    private List<BloqueProgramado> bloques;

    //------------Constructor------------
    public Campanya() {}
    
    //------------Getters y Setters------------  
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public LocalDate getInicio() { return inicio; }
    public void setInicio(LocalDate inicio) { this.inicio = inicio; }
    
    public LocalDate getFin() { return fin; }
    public void setFin(LocalDate fin) { this.fin = fin; }
    
    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }
    
    public String getDemografia() { return demografia; }
    public void setDemografia(String demografia) { this.demografia = demografia; }
    
    public List<Grupo> getGrupos() { return grupos; }
    public void setGrupos(List<Grupo> grupos) { this.grupos = grupos; }
    
    public List<BloqueProgramado> getBloques() { return bloques; }
    public void setBloques(List<BloqueProgramado> bloques) { this.bloques = bloques; }   
    
    //------------Métodos------------
    @Override
    public String toString(){
        return nombre;
    }
}
