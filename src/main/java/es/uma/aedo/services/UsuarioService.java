package es.uma.aedo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import es.uma.aedo.data.entidades.Usuario;
import es.uma.aedo.data.repositorios.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class UsuarioService implements IService<Usuario>{
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repo){
        this.repository = repo;
    }

    public Optional<Usuario> get(String id){
        return repository.findById(id);
    }

    public List<Usuario> getAll(){
        return repository.findAll();
    }

    @Transactional
    public Usuario save(Usuario user){
        return repository.save(user);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public Page<Usuario> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Usuario> list(Pageable pageable, Specification<Usuario> filters) {
        return repository.findAll(filters, pageable);
    }    
    
    public int count() {
        return (int) repository.count();
    }
}
