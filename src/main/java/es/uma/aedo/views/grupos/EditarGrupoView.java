package es.uma.aedo.views.grupos;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.data.entidades.Grupo;
import es.uma.aedo.services.GrupoService;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.OtrasConfig;

@PageTitle("Editar Grupo")
@Route("grupos/editar-grupo/")
public class EditarGrupoView extends Div implements HasUrlParameter<String> {

    private final GrupoService grupoService;
    private Grupo grupo;

    public EditarGrupoView(GrupoService service) {
        this.grupoService = service;
    }

    @Override
    public void setParameter(BeforeEvent event, String id) {
        grupo = (Grupo) OtrasConfig.getEntidadPorParametro(id, grupoService);

        if (grupo != null) {
            setSizeFull();
            VerticalLayout layout = new VerticalLayout(
                    LayoutConfig.createTituloLayout("Editar grupo", "grupos"),
                    GestionGrupo.crearCamposRellenar(grupo, grupoService));

            layout.setSizeFull();
            add(layout);
        } else {
            add(LayoutConfig.createNotFoundLayout());
        }
    }

}
