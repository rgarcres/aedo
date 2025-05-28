package es.uma.aedo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import es.uma.aedo.data.entidades.Grupo;
import es.uma.aedo.data.entidades.Usuario;
import es.uma.aedo.data.repositorios.GrupoRepository;
import es.uma.aedo.data.repositorios.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class GrupoService implements IService<Grupo>{

    private final GrupoRepository repository;
    private final UsuarioRepository usuarioRepository;

    public GrupoService(GrupoRepository repo, UsuarioRepository uRepo){
        this.repository = repo;
        this.usuarioRepository = uRepo;
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
    @Transactional
    public void delete(String id) {
        Grupo grupo = repository.findWithUsuarios(id).orElseThrow();

        if(!grupo.getUsuarios().isEmpty()){
            for(Usuario u: grupo.getUsuarios()){
                Usuario usuario = usuarioRepository.findByIdConGrupos(u.getId()).orElseThrow();
                usuario.removeGrupo(grupo);
                usuarioRepository.save(usuario);
            }
        }

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
