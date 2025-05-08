package es.uma.aedo.data.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import es.uma.aedo.data.entidades.PreguntaTipo3;

public interface PreguntaTipo3Repository   
        extends
            JpaRepository<PreguntaTipo3, String>,
            JpaSpecificationExecutor<PreguntaTipo3> {   
}