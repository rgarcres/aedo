package es.uma.aedo.data.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.uma.aedo.data.entidades.Grupo;

public interface GrupoRepository 
        extends
            JpaRepository<Grupo, String>,
            JpaSpecificationExecutor<Grupo>  {

    @Query("SELECT g FROM Grupo g LEFT JOIN FETCH g.usuarios WHERE g.id = :id")
    Optional<Grupo> findWithUsuarios(@Param("id") String id);
}
