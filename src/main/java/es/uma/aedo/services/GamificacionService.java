package es.uma.aedo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import es.uma.aedo.data.entidades.PreguntaGamificacion;
import es.uma.aedo.data.repositorios.PreguntaGamificacionRepository;

@Service
public class GamificacionService implements IService<PreguntaGamificacion>{

    private final PreguntaGamificacionRepository repository;

    public GamificacionService(PreguntaGamificacionRepository repo){
        this.repository = repo;
    }

    @Override
    public Optional<PreguntaGamificacion> get(String id) {
        return repository.findById(id);
    }

    @Override
    public List<PreguntaGamificacion> getAll() {
        return repository.findAll();
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public PreguntaGamificacion save(PreguntaGamificacion pregunta) {
        return repository.save(pregunta);
    }

    @Override
    public Page<PreguntaGamificacion> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<PreguntaGamificacion> list(Pageable pageable, Specification<PreguntaGamificacion> filters) {
        return repository.findAll(filters, pageable);
    }

    @Override
    public int count() {
        return (int) repository.count();
    }
    
}
