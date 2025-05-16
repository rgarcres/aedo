package es.uma.aedo.views.grupos;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.services.GrupoService;
import es.uma.aedo.views.utilidades.LayoutConfig;

@PageTitle("Crear Grupo")
@Route("grupos/crear-grupo")
public class CrearGrupoView extends Div{
    private final GrupoService grupoService;

    public CrearGrupoView(GrupoService service){
        this.grupoService = service;

        VerticalLayout layout = new VerticalLayout(
            LayoutConfig.createTituloLayout("Crear grupo", "grupos"),
            GestionGrupo.crearCamposRellenar(null, grupoService)
        );
        
        add(layout);
    }
}
