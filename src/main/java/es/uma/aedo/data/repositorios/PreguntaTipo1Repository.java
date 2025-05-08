package es.uma.aedo.data.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import es.uma.aedo.data.entidades.PreguntaTipo1;

public interface PreguntaTipo1Repository   
        extends
            JpaRepository<PreguntaTipo1, String>,
            JpaSpecificationExecutor<PreguntaTipo1> {   
}
