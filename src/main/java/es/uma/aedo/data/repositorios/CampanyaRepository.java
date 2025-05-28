package es.uma.aedo.data.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
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

    @Query("SELECT COUNT(c) FROM Campanya c WHERE MONTH(c.inicio) = :mes AND YEAR(c.inicio) = :ano")
    long countByInicio(@Param("mes") int mes, @Param("ano") int ano);

    @Query("SELECT COUNT(c) FROM Campanya c WHERE MONTH(c.fin) = :mes AND YEAR(c.fin) = :ano")
    long countByFin(@Param("mes") int mes, @Param("ano") int ano);
    
    @EntityGraph(attributePaths = {"bloques", "grupos"})
    @Override
    List<Campanya> findAll();

    @Query("SELECT DISTINCT YEAR(c.inicio) FROM Campanya c WHERE c.inicio IS NOT NULL ORDER BY YEAR(c.inicio)")
    List<Integer> findAniosInicio();
}
