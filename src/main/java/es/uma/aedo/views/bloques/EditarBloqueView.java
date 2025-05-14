package es.uma.aedo.views.bloques;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.data.entidades.Bloque;
import es.uma.aedo.services.BloqueService;
import es.uma.aedo.views.utilidades.LayoutConfig;

@PageTitle("Editar Bloque")
@Route("bloques/editar-bloque/")
public class EditarBloqueView extends Div implements HasUrlParameter<String> {

    private final BloqueService bloqueService;
    private Bloque bloque;

    public EditarBloqueView(BloqueService service) {
        this.bloqueService = service;
    }

    @Override
    public void setParameter(BeforeEvent event, String id) {
        if (id != null) {
            if (bloqueService.get(id).isPresent()) {
                bloque = bloqueService.get(id).get();
                setSizeFull();
                addClassNames("editar-bloque-view");
                VerticalLayout layout = new VerticalLayout(GestionBloque.crearCamposLayout(bloque, bloqueService));

                layout.setSizeFull();
                add(layout);
            } else {
                add(LayoutConfig.createNotFoundLayout());
            }
        } else {
            add(LayoutConfig.createNotFoundLayout());
        }
    }
}
