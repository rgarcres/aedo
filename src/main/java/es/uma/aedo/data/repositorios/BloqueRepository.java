package es.uma.aedo.data.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import es.uma.aedo.data.entidades.Bloque;

public interface BloqueRepository         
        extends
            JpaRepository<Bloque, String>,
            JpaSpecificationExecutor<Bloque> {
}
