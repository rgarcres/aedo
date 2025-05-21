package es.uma.aedo.data.entidades;

import java.time.LocalDate;
import java.util.List;

import es.uma.aedo.data.enumerados.EGenero;
import es.uma.aedo.data.enumerados.ENivelEstudios;
import es.uma.aedo.data.enumerados.ESituacionLaboral;
import es.uma.aedo.data.enumerados.ESituacionPersonal;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Usuario extends AbstractEntity{
    //------------Atributos------------
    private String alias;
    private LocalDate fechaNacimiento;
    @Enumerated(EnumType.STRING)
    private EGenero genero;
    @Enumerated(EnumType.STRING)
    private ENivelEstudios nivelEstudios;
    @Enumerated(EnumType.STRING)
    private ESituacionLaboral situacionLaboral;
    @Enumerated(EnumType.STRING)
    private ESituacionPersonal situacionPersonal;
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;
    @ManyToMany
    @JoinTable(name = "grupo_usuario", 
    joinColumns = @JoinColumn(name="usuario_fk"),
    inverseJoinColumns = @JoinColumn(name="grupo_fk"))
    private List<Grupo> grupos;
    @OneToMany
    private List<Respuesta> respuestas;

    //------------Constructor------------
    public Usuario() {}

    //------------Getters y Setters------------
    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public EGenero getGenero() { return genero; }
    public void setGenero(EGenero genero) { this.genero = genero; }

    public ENivelEstudios getNivelEstudios() { return nivelEstudios; }
    public void setNivelEstudios(ENivelEstudios nivelEstudios) { this.nivelEstudios = nivelEstudios; }

    public ESituacionLaboral getSituacionLaboral() { return situacionLaboral;}
    public void setSituacionLaboral(ESituacionLaboral situacionLaboral) {this.situacionLaboral = situacionLaboral; }

    public ESituacionPersonal getSituacionPersonal() { return situacionPersonal; }
    public void setSituacionPersonal(ESituacionPersonal situacionPersonal) { this.situacionPersonal = situacionPersonal; }

    public Region getRegion() { return region; }
    public void setRegion(Region region) { this.region = region; }

    public List<Grupo> getGrupo() { return grupos; }
    public void setGrupo(List<Grupo> grupos) { this.grupos = grupos; }
    
    public List<Respuesta> getRespuestas() { return respuestas; }
    public void setRespuestas(List<Respuesta> respuestas) { this.respuestas = respuestas; }
    //------------Métodos------------
    @Override
    public String toString(){
        return alias;
    }
}
