package es.uma.aedo.views.utilidades;

import es.uma.aedo.data.entidades.AbstractEntity;
import es.uma.aedo.services.IService;

public class OtrasConfig {
    public static <T> AbstractEntity getEntidadPorParametro(String id, IService<T> service) {
        if (id != null) {
            if (service.get(id).isPresent()) {
                return (AbstractEntity) service.get(id).get();
            } else {
                LayoutConfig.createNotFoundLayout();
                return null;
            }
        } else {
            LayoutConfig.createNotFoundLayout();
            return null;
        }
    }
}
