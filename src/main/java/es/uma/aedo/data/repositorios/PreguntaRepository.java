package es.uma.aedo.data.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import es.uma.aedo.data.entidades.Pregunta;

public interface PreguntaRepository  
        extends
            JpaRepository<Pregunta, String>,
            JpaSpecificationExecutor<Pregunta> {
    
}
