package es.uma.aedo.views.dashboard;

import com.storedobject.chart.Alignment;
import com.storedobject.chart.BarChart;
import com.storedobject.chart.CategoryData;
import com.storedobject.chart.Chart;
import com.storedobject.chart.Color;
import com.storedobject.chart.Data;
import com.storedobject.chart.RectangularCoordinate;
import com.storedobject.chart.RichTextStyle;
import com.storedobject.chart.SOChart;
import com.storedobject.chart.TextStyle;
import com.storedobject.chart.XAxis;
import com.storedobject.chart.YAxis;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.services.BloqueService;
import es.uma.aedo.services.CampanyaService;
import es.uma.aedo.services.GrupoService;
import es.uma.aedo.services.PreguntaService;
import es.uma.aedo.services.RegionService;
import es.uma.aedo.services.UsuarioService;

import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("AEDO Inicio")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.CHART_AREA_SOLID)
public class DashboardView extends Div {

    private final BloqueService bloqueService;
    private final CampanyaService campService;
    private final GrupoService grupoService;
    private final PreguntaService preguntaService;
    private final RegionService regionService;
    private final UsuarioService usuarioService;

    public DashboardView(BloqueService bService, CampanyaService cService, GrupoService gService, PreguntaService pService, RegionService rService, UsuarioService uService) {
        this.bloqueService = bService;
        this.campService = cService;
        this.grupoService = gService;
        this.preguntaService = pService;
        this.regionService = rService;
        this.usuarioService = uService;

        add(crearGrafico());
    }

    private VerticalLayout crearGrafico(){
        setWidthFull();

        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();
        H1 titulo = new H1("AEDO ADMIN");
        addClassName("dashboard-view");
        
        SOChart chart = new SOChart();
        chart.setSize("800px", "500px");

        CategoryData entidades = new CategoryData();
        entidades.add("Bloques");
        entidades.add("Campañas");
        entidades.add("Grupos");
        entidades.add("Preguntas");
        entidades.add("Regiones");
        entidades.add("Usuarios");

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
        barChart.setName("Número de entidades");

        Chart.Label label = barChart.getLabel(true);
        label.setFormatter("{1} {black|{chart}}");
        label.setInside(true);
        label.setGap(15);
        label.setRotation(90);
        label.getPosition().bottom();
        Alignment alignment = label.getAlignment(true);
        alignment.alignCenter();
        alignment.justifyLeft();

        RichTextStyle rich = label.getRichTextStyle(true);
        TextStyle richText = rich.get("black", true);
        richText.setColor(new Color("black"));

        barChart.setLabel(label);

        RectangularCoordinate rc = new RectangularCoordinate();
        barChart.plotOn(rc, ejeX, ejeY);

        chart.add(rc);
        
        layout.setAlignItems(com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER);
        layout.add(titulo, chart);
        return layout;
    }
        // Random random = new Random();

        // CategoryData xValues = new CategoryData();
        // Data yValues1 = new Data(), yValues2 = new Data();

        // for (int x = 0; x <= 11; x++) {
        //     xValues.add("" + (2010 + x));
        //     yValues1.add(random.nextInt(100));
        //     yValues2.add(random.nextInt(100));
        // }

        // XAxis xAxis = new XAxis(xValues);
        // xAxis.setMinAsMinData();
        // YAxis yAxis1 = new YAxis(yValues1), yAxis2 = new YAxis(yValues2);

        // BarChart barChart1 = new BarChart(xValues, yValues1);
        // barChart1.setName("Wheat");
        // BarChart barChart2 = new BarChart(xValues, yValues2);
        // barChart2.setName("Rice");
        // barChart2.setBarGap(0);

        // Chart.Label label = barChart1.getLabel(true);
        // label.setFormatter("{1} {black|{chart}}");
        // label.setInside(true);
        // label.setGap(15);
        // label.setRotation(90);
        // label.getPosition().bottom();
        // Alignment alignment = label.getAlignment(true);
        // alignment.alignCenter();
        // alignment.justifyLeft();

        // RichTextStyle rich = label.getRichTextStyle(true);
        // TextStyle richText = rich.get("black", true);
        // richText.setColor(new Color("black"));

        // barChart2.setLabel(label);

        // RectangularCoordinate rc = new RectangularCoordinate();
        // barChart1.plotOn(rc, xAxis, yAxis1);
        // barChart2.plotOn(rc, xAxis, yAxis2);
        // chart.add(rc);

}
