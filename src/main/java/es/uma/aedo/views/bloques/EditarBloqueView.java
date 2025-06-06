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
import es.uma.aedo.views.utilidades.OtrasConfig;

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
        bloque = (Bloque) OtrasConfig.getEntidadPorParametro(id, bloqueService);

        if (bloque != null) {
            setSizeFull();
            addClassNames("editar-bloque-view");
            VerticalLayout layout = new VerticalLayout(
                LayoutConfig.createTituloLayout("Editar bloque", "bloques"),
                GestionBloque.crearCamposLayout(bloque, bloqueService)
            );

            layout.setSizeFull();
            layout.setPadding(true);
            layout.setSpacing(true);
            add(layout);
        } else {
            add(LayoutConfig.crearNotFoundLayout());
        }
    }
}
