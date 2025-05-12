package es.uma.aedo.services;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import es.uma.aedo.data.entidades.Bloque;
import es.uma.aedo.data.repositorios.BloqueRepository;

@Service
public class BloqueService implements IService<Bloque>{
    
    private final BloqueRepository repository;

    public BloqueService(BloqueRepository repo){
        this.repository = repo;
    }

    @Override
    public Optional<Bloque> get(String id) {
        return repository.findById(id);
    }

    @Override
    public List<Bloque> getAll() {
        return repository.findAll();
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public Bloque save(Bloque bloque) {
        return repository.save(bloque);
    }

    @Override
    public Page<Bloque> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Bloque> list(Pageable pageable, Specification<Bloque> filter) {
        return repository.findAll(filter, pageable);
    }

    @Override
    public int count() {
        return (int) repository.count();
    }
}
