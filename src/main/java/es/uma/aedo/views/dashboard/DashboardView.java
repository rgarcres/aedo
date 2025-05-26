package es.uma.aedo.views.dashboard;

import com.storedobject.chart.Alignment;
import com.storedobject.chart.BarChart;
import com.storedobject.chart.CategoryData;
import com.storedobject.chart.Chart;
import com.storedobject.chart.Color;
import com.storedobject.chart.Data;
import com.storedobject.chart.NightingaleRoseChart;
import com.storedobject.chart.RectangularCoordinate;
import com.storedobject.chart.RichTextStyle;
import com.storedobject.chart.SOChart;
import com.storedobject.chart.TextStyle;
import com.storedobject.chart.Toolbox;
import com.storedobject.chart.XAxis;
import com.storedobject.chart.YAxis;
import com.vaadin.flow.component.formlayout.FormLayout;
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
        setWidthFull();
        addClassName("dashboard-view");

        VerticalLayout layout = new VerticalLayout();
        H1 titulo = new H1("AEDO ADMIN");
        FormLayout graficosLayout = new FormLayout();

        graficosLayout.add(
            crearGraficoCantidades(), 
            crearGraficoCircular(), 
            crearGraficoCantidades(), 
            crearGraficoCantidades()
        );
        
        layout.setAlignItems(com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER);
        layout.setPadding(true);
        layout.setSpacing(true);
        layout.add(titulo, graficosLayout);
        add(layout);
    }

    private VerticalLayout crearGraficoCantidades(){
        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();
        
        SOChart chart = new SOChart();
        chart.setSize("600px", "500px");

        CategoryData entidades = new CategoryData("Bloques", "Campañas","Grupos","Preguntas", "Regiones","Usuarios");

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
        layout.add(chart);
        return layout;
    }

    private VerticalLayout crearGraficoCircular(){
        VerticalLayout layout = new VerticalLayout();
        
        SOChart chart = new SOChart();
        chart.setSize("600px", "500px");
        
        CategoryData entidades = new CategoryData("Bloques", "Campañas","Grupos","Preguntas", "Regiones","Usuarios");
        Data cantidad = new Data();
        cantidad.add(bloqueService.count());
        cantidad.add(campService.count());
        cantidad.add(grupoService.count());
        cantidad.add(preguntaService.count());
        cantidad.add(regionService.count());
        cantidad.add(usuarioService.count());
        // We are going to create a couple of charts. So, each chart should be positioned appropriately
        // Create a self-positioning chart
        NightingaleRoseChart nc = new NightingaleRoseChart(entidades, cantidad);

        // Second chart to add
        // BarChart bc = new BarChart(entidades, cantidad);
        // RectangularCoordinate coordinate =
        //     new RectangularCoordinate(new XAxis(DataType.CATEGORY), new YAxis(DataType.NUMBER));
        // p = new Position();
        // p.setBottom(Size.percentage(55));
        // coordinate.setPosition(p); // Position it leaving 55% space at the bottom
        // bc.plotOn(coordinate); // Bar chart needs to be plotted on a coordinate system

        // Just to demonstrate it, we are creating a "Download" and a "Zoom" toolbox button
        Toolbox toolbox = new Toolbox();
        toolbox.addButton(new Toolbox.Download(), new Toolbox.Zoom());


        // Add the chart components to the chart display area
        chart.add(nc, toolbox);
        layout.setAlignItems(com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER);
        layout.add(chart);
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
