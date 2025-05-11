package es.uma.aedo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import es.uma.aedo.data.entidades.Pregunta;
import es.uma.aedo.data.repositorios.PreguntaRepository;

public class PreguntaService {
    private final PreguntaRepository repository;

    public PreguntaService(PreguntaRepository repo){
        this.repository = repo;
    }

    public Optional<Pregunta> getById(String id){
        return repository.findById(id);
    }

    public List<Pregunta> getAll(){
        return repository.findAll();
    }

    public List<Pregunta> getAllTipo1(){
        return repository.findAllTipo1();
    }
    public List<Pregunta> getAllTipo2(){
        return repository.findAllTipo2();
    }
    public List<Pregunta> getAllTipo3(){
        return repository.findAllTipo3();
    }
    public List<Pregunta> getAllTipo4(){
        return repository.findAllTipo4();
    }

    public Pregunta save(Pregunta p){
        return repository.save(p);
    }

    public void delete(Pregunta p){
        repository.delete(p);
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
