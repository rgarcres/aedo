package es.uma.aedo.data.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import es.uma.aedo.data.entidades.Grupo;

public interface GrupoRepository 
        extends
            JpaRepository<Grupo, String>,
            JpaSpecificationExecutor<Grupo>  {
    
}
