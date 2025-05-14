package es.uma.aedo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import es.uma.aedo.data.entidades.Respuesta;
import es.uma.aedo.data.repositorios.RespuestaRepository;

public class RespuestaService implements IService<Respuesta>{

    private final RespuestaRepository repository;

    public RespuestaService(RespuestaRepository repo){
        this.repository = repo;
    }

    @Override
    public Optional<Respuesta> get(String id) {
        return repository.findById(id);
    }

    @Override
    public List<Respuesta> getAll() {
        return repository.findAll();
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public Respuesta save(Respuesta entity) {
        return repository.save(entity);
    }

    @Override
    public Page<Respuesta> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Respuesta> list(Pageable pageable, Specification<Respuesta> filters) {
        return repository.findAll(filters, pageable);
    }

    @Override
    public int count() {
        return (int) repository.count();
    }
    
}
