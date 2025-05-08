package es.uma.aedo.data.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import es.uma.aedo.data.entidades.PreguntaTipo2;

public interface PreguntaTipo2Repository   
        extends
            JpaRepository<PreguntaTipo2, String>,
            JpaSpecificationExecutor<PreguntaTipo2> {   
}