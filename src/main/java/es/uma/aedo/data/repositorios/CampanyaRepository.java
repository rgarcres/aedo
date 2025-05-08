package es.uma.aedo.data.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import es.uma.aedo.data.entidades.Campanya;

public interface CampanyaRepository 
        extends
            JpaRepository<Campanya, String>,
            JpaSpecificationExecutor<Campanya> {
}
