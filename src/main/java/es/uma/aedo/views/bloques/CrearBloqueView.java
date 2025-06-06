package es.uma.aedo.views.bloques;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.services.BloqueService;
import es.uma.aedo.views.utilidades.LayoutConfig;

@PageTitle("Crear Bloque")
@Route("bloques/crear-bloque")
public class CrearBloqueView extends Div{
    private final BloqueService bloqueService;

    public CrearBloqueView(BloqueService service){
        this.bloqueService = service;
        setSizeFull();
        addClassNames("crear-bloque-view");
        VerticalLayout layout = new VerticalLayout(
            LayoutConfig.crearTituloLayout("Crear bloque", "bloques"),
            GestionBloque.crearCamposLayout(null, bloqueService)
        );

        layout.setSizeFull();
        add(layout);
    }
}
