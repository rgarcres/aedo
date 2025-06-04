package es.uma.aedo.views.dashboard;

import com.storedobject.chart.BarChart;
import com.storedobject.chart.CategoryData;
import com.storedobject.chart.Color;
import com.storedobject.chart.Data;
import com.storedobject.chart.DataType;
import com.storedobject.chart.LineChart;
import com.storedobject.chart.NightingaleRoseChart;
import com.storedobject.chart.RectangularCoordinate;
import com.storedobject.chart.SOChart;
import com.storedobject.chart.XAxis;
import com.storedobject.chart.YAxis;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;

import es.uma.aedo.security.AdminSessionStore;
import es.uma.aedo.security.AdminWebContext;
import es.uma.aedo.services.BloqueService;
import es.uma.aedo.services.CampanyaService;
import es.uma.aedo.services.GrupoService;
import es.uma.aedo.services.PreguntaService;
import es.uma.aedo.services.RegionService;
import es.uma.aedo.services.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

import org.pac4j.core.profile.UserProfile;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("AEDO Inicio")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.CHART_AREA_SOLID)
public class DashboardView extends Div implements BeforeEnterObserver {

    private final BloqueService bloqueService;
    private final CampanyaService campService;
    private final GrupoService grupoService;
    private final PreguntaService preguntaService;
    private final RegionService regionService;
    private final UsuarioService usuarioService;

    public DashboardView(BloqueService bService, CampanyaService cService, GrupoService gService,
            PreguntaService pService, RegionService rService, UsuarioService uService) {
        this.bloqueService = bService;
        this.campService = cService;
        this.grupoService = gService;
        this.preguntaService = pService;
        this.regionService = rService;
        this.usuarioService = uService;
        setWidthFull();
        addClassName("dashboard-view");

        VerticalLayout layout = new VerticalLayout();
        H1 titulo = new H1("AEDO ADMIN");
        FormLayout graficosLayout = new FormLayout();

        graficosLayout.add(
                crearGraficoCantidades(),
                crearGraficoCircular(),
                crearGraficoLineas());

        layout.setAlignItems(com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER);
        layout.setPadding(true);
        layout.setSpacing(true);
        layout.add(titulo, graficosLayout);
        add(layout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        HttpServletRequest request = VaadinServletRequest.getCurrent().getHttpServletRequest();
        HttpServletResponse response = VaadinServletResponse.getCurrent().getHttpServletResponse();

        AdminWebContext context = new AdminWebContext(request, response);
        AdminSessionStore sessionStore = new AdminSessionStore();

        Optional<Object> maybeProfile = sessionStore.get(context, UserProfile.class.getName());

        if (!(maybeProfile.isPresent() && maybeProfile.get() instanceof UserProfile)) {
            // Usuario no autenticado: redirige
            event.forwardTo("iniciar-sesion");
        }
    }

    /*
     * Crea un gráfico de barras que muestra
     * el número que hay de cada entidad de la aplicación
     */
    private VerticalLayout crearGraficoCantidades() {
        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();

        H4 titulo = new H4("Número de entidades de cada tipo");
        SOChart chart = new SOChart();
        chart.setSize("600px", "500px");

        CategoryData entidades = new CategoryData("Bloques", "Campañas", "Grupos", "Preguntas", "Regiones", "Usuarios");

        Data cantidad = new Data();
        cantidad.add(bloqueService.count());
        cantidad.add(campService.count());
        cantidad.add(grupoService.count());
        cantidad.add(preguntaService.count());
        cantidad.add(regionService.count());
        cantidad.add(usuarioService.count());

        XAxis ejeX = new XAxis(entidades);
        ejeX.setMinAsMinData();
        YAxis ejeY = new YAxis(cantidad);

        BarChart barChart = new BarChart(entidades, cantidad);
        barChart.setName("Nº entidades");

        RectangularCoordinate rc = new RectangularCoordinate();
        barChart.setColors(new Color("#6654ff"));
        barChart.plotOn(rc, ejeX, ejeY);

        chart.add(rc);

        layout.setAlignItems(com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER);
        layout.add(titulo, chart);
        return layout;
    }

    /*
     * Crea un gráfico circular en el que se muestra
     * el número de usuarios que hay por provincias
     */
    private VerticalLayout crearGraficoCircular() {
        VerticalLayout layout = new VerticalLayout();

        SOChart chart = new SOChart();
        chart.setSize("600px", "500px");

        H4 titulo = new H4("Usuarios por provincia");

        CategoryData provincias = new CategoryData();
        Data cantidad = new Data();

        // Contar usuarios por provincia
        for (String pro : regionService.getAllProvincias()) {
            provincias.add(pro);
            cantidad.add(usuarioService.countPorProvincia(pro));
        }

        NightingaleRoseChart nc = new NightingaleRoseChart(provincias, cantidad);

        chart.add(nc);
        layout.setAlignItems(com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER);
        layout.add(titulo, chart);
        return layout;
    }

    /*
     * Crea un gráfico de líneas que muestra
     * dos líneas que representan las fechas en las que
     * empieza una campaña (morada)
     * termina una campaña (amarilla)
     */
    private VerticalLayout crearGraficoLineas() {
        // ----------------Inicializar componentes----------------
        VerticalLayout layout = new VerticalLayout();

        SOChart chart = new SOChart();
        chart.setSize("600px", "500px");

        H4 titulo = new H4("Campañas por fechas de inicio y fin");

        // ----------------Inicializar datos----------------
        CategoryData fechas = new CategoryData();
        Data numCampInicio = new Data();
        Data numCampFin = new Data();

        // ----------------Rellenar los datos----------------
        for (Integer anio : campService.getAllAnios()) {
            for (int i = 1; i <= 12; i++) {
                if (i < 10) {
                    fechas.add("0" + i + "/" + anio);
                } else {
                    fechas.add(i + "/" + anio);
                }
                numCampInicio.add(campService.countInicio(i, anio));
                numCampFin.add(campService.countFin(i, anio));
            }
        }

        // ----------------Definir ejes----------------
        XAxis ejeX = new XAxis(DataType.CATEGORY);
        YAxis ejeY = new YAxis(DataType.NUMBER);

        // ----------------Definir coordenadas----------------
        RectangularCoordinate rc = new RectangularCoordinate(ejeX, ejeY);

        // ----------------Crear gráficos de líneas----------------
        // Inicio
        LineChart lineInicio = new LineChart(fechas, numCampInicio);
        lineInicio.setName("Campañas iniciadas");
        lineInicio.setColors(new Color("#6654ff"));
        lineInicio.plotOn(rc);

        // Fin
        LineChart lineFin = new LineChart(fechas, numCampFin);
        lineFin.setName("Campañas finalizadas");
        lineFin.setColors(new Color("#ffd000"));
        lineFin.plotOn(rc);

        chart.add(lineInicio, lineFin);
        layout.setAlignItems(com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER);
        layout.add(titulo, chart);
        return layout;
    }
}
