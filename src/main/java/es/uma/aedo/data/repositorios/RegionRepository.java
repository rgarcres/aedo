package es.uma.aedo.data.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import es.uma.aedo.data.entidades.Region;

public interface RegionRepository extends JpaRepository<Region, String>, JpaSpecificationExecutor<Region>  {
    @Query("SELECT DISTINCT r.localidad FROM Region r")
    List<String> findAllLocalidades();
    @Query("SELECT DISTINCT r.provincia FROM Region r")
    List<String> findAllProvincias();
    @Query("SELECT DISTINCT r.comunidadAutonoma FROM Region r")
    List<String> findAllComunidades();
}
