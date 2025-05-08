package es.uma.aedo.data.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import es.uma.aedo.data.entidades.Usuario;

public interface UsuarioRepository 
        extends 
            JpaRepository<Usuario, String>,
            JpaSpecificationExecutor<Usuario> {
}
