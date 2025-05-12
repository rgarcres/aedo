package es.uma.aedo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface IService<T> {
    Optional<T> get(String id);
    List<T> getAll();
    void delete(String id);
    T save(T entity);
    Page<T> list(Pageable pageable);
    Page<T> list(Pageable pageable, Specification<T> filters);
    int count();
}
