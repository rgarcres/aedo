package es.uma.aedo.data.repositorios;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import es.uma.aedo.data.entidades.Bloque;
import es.uma.aedo.data.entidades.Pregunta;

public interface PreguntaRepository  
        extends
            JpaRepository<Pregunta, String>,
            JpaSpecificationExecutor<Pregunta> {
    @Query("SELECT DISTINCT p.bloque FROM Pregunta p")
    List<Bloque> findAllBloques();
}
