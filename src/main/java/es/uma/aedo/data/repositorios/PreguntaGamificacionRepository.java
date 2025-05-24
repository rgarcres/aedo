package es.uma.aedo.data.repositorios;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import es.uma.aedo.data.entidades.PreguntaGamificacion;

public interface PreguntaGamificacionRepository  
        extends
            JpaRepository<PreguntaGamificacion, String>,
            JpaSpecificationExecutor<PreguntaGamificacion> {
    // @Query("SELECT p FROM PreguntaGamificacion p LEFT JOIN FETCH p.posiblesRespuestas WHERE p.id = :id")
    // Optional<PreguntaGamificacion> findWithRespuestas(@Param("id") String id);
}
