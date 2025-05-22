package es.uma.aedo.data.repositorios;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.uma.aedo.data.entidades.Usuario;

public interface UsuarioRepository 
        extends 
            JpaRepository<Usuario, String>,
            JpaSpecificationExecutor<Usuario> {
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.grupos WHERE u.id = :id")
    Optional<Usuario> findByIdConGrupos(@Param("id") String id);
    
    @EntityGraph(attributePaths = {"grupos", "region"})
    @Override
    Page<Usuario> findAll(Specification<Usuario> spec, Pageable pageable);
}
