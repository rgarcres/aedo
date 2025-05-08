package es.uma.aedo.data.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import es.uma.aedo.data.entidades.PreguntaGamificacion;

public interface PreguntaGamificacionRepository  
        extends
            JpaRepository<PreguntaGamificacion, String>,
            JpaSpecificationExecutor<PreguntaGamificacion> {
}
