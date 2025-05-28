package es.uma.aedo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import es.uma.aedo.data.entidades.Bloque;
import es.uma.aedo.data.entidades.Pregunta;
import es.uma.aedo.data.repositorios.PreguntaRepository;
import jakarta.transaction.Transactional;

@Service
public class PreguntaService implements IService<Pregunta> {
    private final PreguntaRepository repository;

    public PreguntaService(PreguntaRepository repo){
        this.repository = repo;
    }

    public Optional<Pregunta> get(String id){
        return repository.findById(id);
    }

    @Transactional
    public Optional<Pregunta> getConOpciones(String id){
        return repository.findWithOpciones(id);
    }

    public List<Pregunta> getAll(){
        return repository.findAll();
    }

    public List<Bloque> getAllBloques(){
        return repository.findAllBloques();
    }
    public Pregunta save(Pregunta p){
        return repository.save(p);
    }

    public void delete(String id){
        repository.deleteById(id);
    }

    public Page<Pregunta> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Pregunta> list(Pageable pageable, Specification<Pregunta> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count(){
        return (int) repository.count();
    }
}
