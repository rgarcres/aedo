package es.uma.aedo.services;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import es.uma.aedo.data.entidades.Bloque;
import es.uma.aedo.data.repositorios.BloqueRepository;

@Service
public class BloqueService {
    
    private BloqueRepository repository;

    public BloqueService(BloqueRepository repo){
        this.repository = repo;
    }

    public Page<Bloque> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Bloque> list(Pageable pageable, Specification<Bloque> filter) {
        return repository.findAll(filter, pageable);
    }
}
