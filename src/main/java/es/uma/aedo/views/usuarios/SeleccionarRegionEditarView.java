package es.uma.aedo.views.usuarios;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import es.uma.aedo.data.entidades.Region;
import es.uma.aedo.data.entidades.Usuario;
import es.uma.aedo.services.RegionService;
import es.uma.aedo.services.UsuarioService;
import es.uma.aedo.views.regiones.GestionRegion;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;
import es.uma.aedo.views.utilidades.OtrasConfig;

@PageTitle("Seleccionar Grupo")
@Route("usuarios/editar-usuario/seleccionar-region")
public class SeleccionarRegionEditarView extends Div implements HasUrlParameter<String> {
    private Grid<Region> grid;

    private Usuario usuario;
    private Region regionSeleccionada;
    private final UsuarioService usuarioService;
    private final RegionService regionService;

    public SeleccionarRegionEditarView(UsuarioService uService, RegionService rService) {
        this.usuarioService = uService;
        this.regionService = rService;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String id) {
        usuario = (Usuario) OtrasConfig.getEntidadPorParametro(id, usuarioService);
        if (usuario != null) {
            setWidthFull();
            addClassName("usuarios-seleccionar-region-view");

            GestionRegion.Filters filters = new GestionRegion.Filters(() -> refreshGrid(), regionService);

            grid = GestionRegion.crearGrid(regionService, filters);
            grid.addItemClickListener(e -> {
                regionSeleccionada = e.getItem();
            });

            if (usuario.getRegion() != null) {
                grid.select(usuario.getRegion());
            }

            VerticalLayout layout = new VerticalLayout(
                LayoutConfig.createTituloLayout("Seleccionar region", "usuarios/editar-usuario/" + id),
                filters,
                grid,
                crearBotonesLayout()
            );

            layout.setAlignItems(Alignment.CENTER);
            layout.setPadding(true);
            layout.setSpacing(true);
            add(layout);
        } else {
            add(LayoutConfig.createNotFoundLayout());
        }
    }

    private HorizontalLayout crearBotonesLayout() {
        HorizontalLayout layout = new HorizontalLayout();

        Button siguiente = BotonesConfig.crearBotonPrincipal("Siguiente");
        Button cancelar = BotonesConfig.crearBotonSecundario("Cancelar");

        siguiente.addClickListener(e -> {
            if (regionSeleccionada != null) {
                usuario.setRegion(regionSeleccionada);
                usuarioService.save(usuario);
                siguiente.getUI()
                        .ifPresent(ui -> ui.navigate("usuarios/editar-usuario/seleccionar-grupos/" + usuario.getId()));
            } else {
                NotificacionesConfig.crearNotificacionError("Selecciona una región",
                        "No hay ninguna región seleccionada");
            }
        });

        cancelar.addClickListener(e -> {
            usuarioService.delete(usuario.getId());
            if (VaadinSession.getCurrent().getAttribute("usuarioEditar") != null) {
                Usuario user = (Usuario) VaadinSession.getCurrent().getAttribute("usuarioEditar");
                usuarioService.save(user);
            }
            cancelar.getUI().ifPresent(ui -> ui.navigate("usuarios"));
        });

        layout.setAlignItems(Alignment.CENTER);
        layout.add(siguiente, cancelar);
        return layout;
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }
}
