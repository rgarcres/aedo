package es.uma.aedo.views.gamificacion;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uma.aedo.data.entidades.PreguntaGamificacion;
import es.uma.aedo.services.GamificacionService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;
import es.uma.aedo.views.utilidades.OtrasConfig;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class GestionGamificacion {
    public static class Filters extends Div implements Specification<PreguntaGamificacion> {
        // ------------Campos de filtros------------
        private final TextField enunciadoField = new TextField("Enunciado");

        public Filters(Runnable onSearch) {
            HorizontalLayout filtrosLayout = new HorizontalLayout();
            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER
            );

            List<HasValue<?, ?>> fields = new ArrayList<>();
            fields.add(enunciadoField);

            Div actions = LayoutConfig.crearBotonesFiltros(onSearch, fields);

            filtrosLayout.add(enunciadoField, actions);
            add(filtrosLayout);
        }

        @Override
        public Predicate toPredicate(Root<PreguntaGamificacion> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if(!enunciadoField.isEmpty()){
                String enunciado = enunciadoField.getValue().toLowerCase();
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("enunciado")), 
                        "%" + enunciado + "%"
                    )
                );
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }
    }

    public static VerticalLayout crearCamposLayout(GamificacionService gamificacionService, PreguntaGamificacion pg){
        VerticalLayout layout = new VerticalLayout();
        FormLayout camposLayout = new FormLayout();
        HorizontalLayout botonesLayout = new HorizontalLayout();

        TextField idField = new TextField("ID*");
        TextField enunciadoField = new TextField("Enunciado*");

        Button aplicar = BotonesConfig.crearBotonPrincipal("Aplicar");
        Button cancelar = BotonesConfig.crearBotonSecundario("Cancelar", "gamificacion");

        if(pg != null){
            idField.setValue(pg.getId());
            enunciadoField.setValue(pg.getEnunciado());
        }

        aplicar.addClickListener(e -> {
            String id = idField.getValue();
            String enunciado = enunciadoField.getValue();
            
            boolean exito = crearPregunta(gamificacionService, pg, id, enunciado);

            if(exito){
                aplicar.getUI().ifPresent(ui -> ui.navigate("gamificacion"));
            }
        });

        camposLayout.add(idField, enunciadoField);
        botonesLayout.add(aplicar, cancelar);
        layout.setAlignItems(Alignment.CENTER);
        layout.add(camposLayout, botonesLayout);
        return layout;
    }

    public static Grid<PreguntaGamificacion> crearGrid(GamificacionService gamificacionService,
            Specification<PreguntaGamificacion> filters) {
        Grid<PreguntaGamificacion> grid = new Grid<>(PreguntaGamificacion.class, false);
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("enunciado").setAutoWidth(true);
        //grid.addColumn(pg -> pg.getPosiblesRespuestas().stream().toString()).setAutoWidth(true);
        grid.addColumn("respuestaCorrecta").setAutoWidth(true);

        grid.setItems(query -> gamificacionService.list(VaadinSpringDataHelpers.toSpringPageRequest(query), filters)
                .stream());

        return grid;
    }

    private static boolean crearPregunta(GamificacionService gamificacionService, PreguntaGamificacion pg, String id,
            String enunciado) {
        if(comprobarCamposVacios(id, enunciado)){
            NotificacionesConfig.crearNotificacionError("Campos vacíos", "Todos los campos deben estar completos");
            return false;
        } else if(OtrasConfig.comprobarId(id, gamificacionService, pg)){
            NotificacionesConfig.crearNotificacionError("El ID ya existe", "Introduzca un nuevo ID que sea único");
            return false;
        } else {
            if(pg == null){
                PreguntaGamificacion nuevaPg = new PreguntaGamificacion();
                nuevaPg.setId(id);
                nuevaPg.setEnunciado(enunciado);
                gamificacionService.save(nuevaPg);
            } else {
                pg.setId(id);
                pg.setEnunciado(enunciado);
                gamificacionService.save(pg);
            }
            return true;
        }        
    }

    private static boolean comprobarCamposVacios(String id, String enunciado) {
        return id.isBlank() || enunciado.isBlank();
    }
}
