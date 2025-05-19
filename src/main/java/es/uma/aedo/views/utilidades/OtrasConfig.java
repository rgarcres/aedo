package es.uma.aedo.views.utilidades;

import java.time.LocalDate;

import es.uma.aedo.data.entidades.AbstractEntity;
import es.uma.aedo.services.IService;

public class OtrasConfig {

    /*
     * Devuelve la entidad con ID pasado por parámetro.
     * En caso de no existir, devuelve null
     */
    public static <T> AbstractEntity getEntidadPorParametro(String id, IService<T> service) {
        if (id != null) {
            if (service.get(id).isPresent()) {
                return (AbstractEntity) service.get(id).get();
            }
            return null;

        }
        return null;
    }

    /*
     * Devuelve true si el ID pasado por parámetro ya existe en la BD y ese ID no es
     * el mismo ID que la propia entidad que se ha pasado por parámetro
     */
    public static <T> boolean comprobarId(String id, IService<T> service, AbstractEntity entity){
        if(entity == null){
            return service.get(id).isPresent();
        } else {
            return service.get(id).isPresent() && !id.equals(entity.getId());
        }
    }

    /*
     * Devuelve true si la fecha si la fecha pasada por parámetro es anterior a
     * la fecha de hoy
     */
    public static boolean comprobarFecha(LocalDate date){
        return date.isBefore(LocalDate.now());
    }
}
