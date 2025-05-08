package es.uma.aedo.data.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import es.uma.aedo.data.entidades.Region;

public interface RegionRepository  
        extends
            JpaRepository<Region, String>,
            JpaSpecificationExecutor<Region>  {
    
}
