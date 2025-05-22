package es.uma.aedo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import es.uma.aedo.data.entidades.Campanya;
import es.uma.aedo.data.repositorios.CampanyaRepository;
import jakarta.transaction.Transactional;

@Service
public class CampanyaService implements IService<Campanya>{

    private final CampanyaRepository repository;

    public CampanyaService(CampanyaRepository repo){
        this.repository = repo;
    }

    @Override
    public Optional<Campanya> get(String id) {
        return repository.findById(id);
    }

    public Optional<Campanya> getConGrupo(String id){
        return repository.findWithGrupo(id);
    }

    public Optional<Campanya> getConBloque(String id){
        return repository.findWithBloque(id);
    }

    @Override
    public List<Campanya> getAll() {
        return repository.findAll();
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public Campanya save(Campanya entity) {
        return repository.save(entity);
    }

    @Override
    public Page<Campanya> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Campanya> list(Pageable pageable, Specification<Campanya> filters) {
        return repository.findAll(filters, pageable);
    }

    @Override
    public int count() {
        return (int) repository.count();
    }
    
}
