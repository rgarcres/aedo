package es.uma.aedo.data.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import es.uma.aedo.data.entidades.BloqueProgramado;

public interface BloqueProgramadoRepository 
    extends 
        JpaRepository<BloqueProgramado, String>,
        JpaSpecificationExecutor<BloqueProgramado>{
}
