package es.uma.aedo.data;

import java.time.LocalDate;

import es.uma.aedo.data.enumerados.EGenero;
import es.uma.aedo.data.enumerados.ENivelEstudios;
import es.uma.aedo.data.enumerados.ESituacionLaboral;
import es.uma.aedo.data.enumerados.ESituacionPersonal;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Usuario extends AbstractEntity{
    //------------Atributos------------
    private String alias;
    private Long passHash;
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
    @JoinColumn(name = "regionID")
    private Region region;
    @ManyToOne
    @JoinColumn(name="grupoID")
    private Grupo grupo;
    @ManyToOne
    @JoinColumn(name="campID")
    private Campanya camp;

    //------------Constructor------------
    public Usuario() {}

    //------------Getters y Setters------------
    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }
    
    public Long getPassHash() { return passHash; }
    public void setPassHash(Long passHash) { this.passHash = passHash; }

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

    public Grupo getGrupo() { return grupo; }
    public void setGrupo(Grupo grupo) { this.grupo = grupo; }

    //------------Métodos------------
    @Override
    public String toString(){
        return alias;
    }
}
