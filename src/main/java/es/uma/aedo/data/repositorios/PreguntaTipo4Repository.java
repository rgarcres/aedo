package es.uma.aedo.data.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import es.uma.aedo.data.entidades.PreguntaTipo4;

public interface PreguntaTipo4Repository   
        extends
            JpaRepository<PreguntaTipo4, String>,
            JpaSpecificationExecutor<PreguntaTipo4> {   
}