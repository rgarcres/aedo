package es.uma.aedo.data.repositorios;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import es.uma.aedo.data.entidades.Respuesta;

public interface RespuestaRepository  
        extends
            JpaRepository<Respuesta, String>,
            JpaSpecificationExecutor<Respuesta> {
}
