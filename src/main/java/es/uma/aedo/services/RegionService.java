package es.uma.aedo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import es.uma.aedo.data.entidades.Region;
import es.uma.aedo.data.repositorios.RegionRepository;
import jakarta.transaction.Transactional;

@Service
public class RegionService implements IService<Region> {
    private final RegionRepository repository;

    public RegionService(RegionRepository repository){
        this.repository = repository;
    }

    public Optional<Region> get(String ID){
        return repository.findById(ID);
    }

    public List<Region> getAll(){
        return repository.findAll();
    }

    public List<String> getAllLocalidades(){
        return repository.findAllLocalidades();
    }
    public List<String> getAllProvincias(){
        return repository.findAllProvincias();
    }
    public List<String> getAllComunidades(){
        return repository.findAllComunidades();
    }

    @Transactional
    public Region save(Region region){
        return repository.save(region);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public Page<Region> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Region> list(Pageable pageable, Specification<Region> filters) {
        return repository.findAll(filters, pageable);
    }    
    
    public int count() {
        return (int) repository.count();
    }
}
