package es.uma.aedo.data.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.uma.aedo.data.entidades.Campanya;

public interface CampanyaRepository 
        extends
            JpaRepository<Campanya, String>,
            JpaSpecificationExecutor<Campanya> {

    @Query("SELECT c FROM Campanya c LEFT JOIN FETCH c.grupos WHERE c.id = :id")
    Optional<Campanya> findWithGrupo(@Param("id") String id);

    @Query("SELECT c FROM Campanya c LEFT JOIN FETCH c.bloques WHERE c.id = :id")
    Optional<Campanya> findWithBloque(@Param("id") String id);
}
