package es.uma.aedo.views.bloques;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;


import es.uma.aedo.data.entidades.Bloque;
import es.uma.aedo.services.BloqueService;

@PageTitle("Editar Bloque")
@Route("bloques/editar-bloque")
public class EditarBloqueView extends Div{
    
    private final BloqueService bloqueService;
    private Bloque bloque;

    public EditarBloqueView(BloqueService service) {
        this.bloqueService = service;
        bloque = (Bloque) VaadinSession.getCurrent().getAttribute("bloqueEditar");
        
        setSizeFull();
        addClassNames("editar-bloque-view");
        VerticalLayout layout = new VerticalLayout(CrearEditarBloque.crearCamposLayout(bloque, bloqueService));

        layout.setSizeFull();
        add(layout);
    }
}
