package es.uma.aedo.views.campanas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uma.aedo.data.entidades.Campanya;
import es.uma.aedo.services.CampanyaService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;
import es.uma.aedo.views.utilidades.OtrasConfig;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class GestionCamp {

    public static class Filters extends Div implements Specification<Campanya> {
        // ------------Componentes------------
        private final TextField nombreField = new TextField("Nombre");
        private final DatePicker inicioPicker = new DatePicker("Inicio");
        private final DatePicker finPicker = new DatePicker("Fin");

        public Filters(Runnable onSearch) {
            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);
            getStyle().set("display", "flex");
            getStyle().set("gap", "1rem"); // Opcional: separa los componentes

            inicioPicker.setAriaLabel("Inicio desde...");
            finPicker.setAriaLabel("Fin hasta...");
            inicioPicker.addValueChangeListener(e -> {
                finPicker.setMin(inicioPicker.getValue().plusDays(1));
            });
            List<HasValue<?, ?>> fields = new ArrayList<>();
            fields.add(nombreField);
            fields.add(inicioPicker);
            fields.add(finPicker);

            Div actions = LayoutConfig.crearBotonesFiltros(onSearch, fields);

            add(nombreField, LayoutConfig.crearRangoFechas(inicioPicker, finPicker), actions);
        }

        @Override
        public Predicate toPredicate(Root<Campanya> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if (!nombreField.isEmpty()) {
                String nombreMinus = nombreField.getValue().toLowerCase();
                Predicate p = criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")),
                        "%" + nombreMinus + "%");
                predicates.add(p);
            }

            if (!inicioPicker.isEmpty()) {
                LocalDate inicio = inicioPicker.getValue();
                Predicate p = criteriaBuilder.greaterThanOrEqualTo(
                        root.get("inicio"), criteriaBuilder.literal(inicio));

                predicates.add(p);
            }

            if (!finPicker.isEmpty()) {
                LocalDate fin = finPicker.getValue();

                Predicate p = criteriaBuilder.greaterThanOrEqualTo(
                        criteriaBuilder.literal(fin), root.get("inicio"));

                predicates.add(p);
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }

    }

    public static Grid<Campanya> crearGrid(CampanyaService service, Specification<Campanya> filters) {
        Grid<Campanya> grid = new Grid<>(Campanya.class, false);
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("nombre").setAutoWidth(true);
        grid.addColumn("inicio").setAutoWidth(true);
        grid.addColumn("fin").setAutoWidth(true);
        grid.addColumn("objetivo").setAutoWidth(true);
        grid.addColumn("demografia").setAutoWidth(true);

        grid.setItems(query -> service.list(VaadinSpringDataHelpers.toSpringPageRequest(query), filters)
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }

    public static VerticalLayout crearCamposLayout(CampanyaService service, Campanya camp) {
        VerticalLayout layout = new VerticalLayout();
        FormLayout camposLayout = new FormLayout();
        HorizontalLayout botonesLayout = new HorizontalLayout();

        TextField idField = new TextField("ID*");
        TextField nombreField = new TextField("Nombre*");
        DatePicker inicioPicker = new DatePicker("Inicio*");
        DatePicker finPicker = new DatePicker("Fin*");
        TextField objetivosField = new TextField("Objetivos de la campaña");
        TextField demografiaField = new TextField("Demografía de la muestra");

        Button aplicar = BotonesConfig.crearBotonPrincipal("Aplicar");
        Button cancelar = BotonesConfig.crearBotonSecundario("Cancelar", "campañas");

        if(camp != null){
            idField.setValue(camp.getId());
            nombreField.setValue(camp.getNombre());
            inicioPicker.setValue(camp.getInicio());
            finPicker.setValue(camp.getFin());
            objetivosField.setValue(camp.getObjetivo());
            demografiaField.setValue(camp.getDemografia());
        }

        inicioPicker.addValueChangeListener(e -> {
            finPicker.setMin(inicioPicker.getValue().plusDays(1));
        });

        aplicar.addClickListener(e -> {
            String id = idField.getValue();
            String nombre = nombreField.getValue();
            LocalDate inicio = inicioPicker.getValue();
            LocalDate fin = finPicker.getValue();
            String objetivos = objetivosField.getValue();
            String demografia = demografiaField.getValue();
            
            boolean exito = crearCampana(service, camp, id, nombre, inicio, fin, objetivos, demografia);

            if(exito){
                aplicar.getUI().ifPresent(ui -> ui.navigate("campañas"));
            }
            
        });

        camposLayout.add(idField, nombreField, inicioPicker, finPicker, objetivosField, demografiaField);
        botonesLayout.add(aplicar, cancelar);
        layout.setAlignItems(Alignment.CENTER);
        layout.add(camposLayout, botonesLayout);

        return layout;
    }

    private static boolean crearCampana(CampanyaService service, Campanya camp, String id, String nombre, LocalDate inicio, LocalDate fin, String objetivos, String demografia) 
    {
        if(comprobarCampos(id, nombre, inicio, fin)){
            NotificacionesConfig.crearNotificacionError("Campos vacíos", "Ninguno de los campos marcados con *  puede estar vacío");
            return false;
        } else if(inicio.isAfter(fin)){
            NotificacionesConfig.crearNotificacionError("Fechas incorrectas", "La fecha de inicio no puede ser después de la fecha de fin");
            return false;
        } else if(OtrasConfig.comprobarId(id, service, camp)){
            NotificacionesConfig.crearNotificacionError("El ID ya existe", "Introduzca un nuevo ID que sea único");
            return false;
        } else {
            if(camp == null){
                Campanya c = new Campanya();
                c.setId(id);
                c.setNombre(nombre);
                c.setInicio(inicio);
                c.setFin(fin);
                c.setObjetivo(objetivos);
                c.setDemografia(demografia);
                service.save(c);
                NotificacionesConfig.crearNotificacionExito("¡Campaña creada!", "La campaña ha sido creada con éxito");
            } else {
                camp.setId(id);
                camp.setNombre(nombre);
                camp.setInicio(inicio);
                camp.setFin(fin);
                camp.setObjetivo(objetivos);
                camp.setDemografia(demografia);
                service.save(camp);
                    NotificacionesConfig.crearNotificacionExito("¡Campaña editada!", "La campaña ha sido editada con éxito");
            }
            return true;
        }        
    }

    private static boolean comprobarCampos(String id, String nombre, LocalDate inicio, LocalDate fin) {
        return id.isBlank() || nombre.isBlank() || inicio == null || fin == null;
    }

    
}
