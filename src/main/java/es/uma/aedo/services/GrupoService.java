package es.uma.aedo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import es.uma.aedo.data.entidades.Grupo;
import es.uma.aedo.data.repositorios.GrupoRepository;

@Service
public class GrupoService implements IService<Grupo>{

    private final GrupoRepository repository;

    public GrupoService(GrupoRepository repo){
        this.repository = repo;
    }

    @Override
    public Optional<Grupo> get(String id) {
        return repository.findById(id);
    }

    public Optional<Grupo> getConUsuarios(String id){
        return repository.findWithUsuarios(id);
    }

    @Override
    public List<Grupo> getAll() {
        return repository.findAll();
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public Grupo save(Grupo grupo) {
        return repository.save(grupo);
    }

    @Override
    public Page<Grupo> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Grupo> list(Pageable pageable, Specification<Grupo> filters) {
        return repository.findAll(filters, pageable);
    }

    @Override
    public int count() {
        return (int) repository.count();
    }
    
}
