package es.uma.aedo.data.repositorios;

import java.util.Optional;

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
}
