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
            crearGraficoLineas(), 
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
        
        H4 titulo = new H4("Número de entidades de cada tipo");
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
        barChart.setName("Nº entidades");
        // Chart.Label label = barChart.getLabel(true);
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

        // barChart.setLabel(label);

        RectangularCoordinate rc = new RectangularCoordinate();
        barChart.setColors(new Color("#6654ff"));
        barChart.plotOn(rc, ejeX, ejeY);

        chart.add(rc);
        
        layout.setAlignItems(com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER);
        layout.add(titulo, chart);
        return layout;
    }

    private VerticalLayout crearGraficoCircular(){
        VerticalLayout layout = new VerticalLayout();

        SOChart chart = new SOChart();
        chart.setSize("600px", "500px");
        
        H4 titulo = new H4("Usuarios por provincia");
        
        CategoryData provincias = new CategoryData();
        Data cantidad = new Data();

        // Contar usuarios por provincia
        for(String pro: regionService.getAllProvincias()){
            provincias.add(pro);
            cantidad.add(usuarioService.countPorProvincia(pro));
        }

        NightingaleRoseChart nc = new NightingaleRoseChart(provincias, cantidad);

        chart.add(nc);
        layout.setAlignItems(com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER);
        layout.add(titulo, chart);
        return layout;
    }

    private VerticalLayout crearGraficoLineas(){
        VerticalLayout layout = new VerticalLayout();

        SOChart chart = new SOChart();
        chart.setSize("600px", "500px");
        
        H4 titulo = new H4("Campañas por fechas de inicio y fin");
        
        CategoryData fechas = new CategoryData();
        Data numCampInicio = new Data();
        Data numCampFin = new Data();

        for(Integer anio: campService.getAllAnios()){
            for(int i = 1; i <= 12; i++){
                if(i < 10){
                    fechas.add("0"+i+"/"+anio);
                } else {
                    fechas.add(i+"/"+anio);
                }
                numCampInicio.add(campService.countInicio(i, anio));
                numCampFin.add(campService.countFin(i, anio));
            }
        }

        XAxis ejeX = new XAxis(DataType.CATEGORY);
        YAxis ejeY = new YAxis(DataType.NUMBER);

        RectangularCoordinate rc = new RectangularCoordinate(ejeX, ejeY);

        //----------------Crear gráficos de líneas----------------
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
