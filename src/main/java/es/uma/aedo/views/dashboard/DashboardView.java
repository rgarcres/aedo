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

import java.util.Random;

import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("AEDO Inicio")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.CHART_AREA_SOLID)
public class DashboardView extends Div {

    public DashboardView() {
        setWidthFull();

        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();
        H1 titulo = new H1("AEDO ADMIN");
        addClassName("dashboard-view");
        // Construcción directa del gráfico
        SOChart chart = new SOChart();
        chart.setSize("800px", "500px");

        Random random = new Random();
        CategoryData xValues = new CategoryData();
        Data yValues1 = new Data(), yValues2 = new Data();

        for (int x = 0; x <= 11; x++) {
            xValues.add("" + (2010 + x));
            yValues1.add(random.nextInt(100));
            yValues2.add(random.nextInt(100));
        }

        XAxis xAxis = new XAxis(xValues);
        xAxis.setMinAsMinData();
        YAxis yAxis1 = new YAxis(yValues1), yAxis2 = new YAxis(yValues2);

        BarChart barChart1 = new BarChart(xValues, yValues1);
        barChart1.setName("Wheat");
        BarChart barChart2 = new BarChart(xValues, yValues2);
        barChart2.setName("Rice");
        barChart2.setBarGap(0);

        Chart.Label label = barChart1.getLabel(true);
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

        barChart2.setLabel(label);

        RectangularCoordinate rc = new RectangularCoordinate();
        barChart1.plotOn(rc, xAxis, yAxis1);
        barChart2.plotOn(rc, xAxis, yAxis2);
        chart.add(rc);

        layout.setAlignItems(com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER);
        layout.add(titulo, chart);
        add(layout);
    }

    // private Component createViewEvents() {
    // // Header
    // Select year = new Select();
    // year.setItems("2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018",
    // "2019", "2020", "2021");
    // year.setValue("2021");
    // year.setWidth("100px");

    // HorizontalLayout header = createHeader("View events", "City/month");
    // header.add(year);

    // // Chart
    // Chart chart = new Chart(ChartType.AREASPLINE);
    // Configuration conf = chart.getConfiguration();
    // conf.getChart().setStyledMode(true);

    // XAxis xAxis = new XAxis();
    // xAxis.setCategories("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
    // "Sep", "Oct", "Nov", "Dec");
    // conf.addxAxis(xAxis);

    // conf.getyAxis().setTitle("Values");

    // PlotOptionsAreaspline plotOptions = new PlotOptionsAreaspline();
    // plotOptions.setPointPlacement(PointPlacement.ON);
    // plotOptions.setMarker(new Marker(false));
    // conf.addPlotOptions(plotOptions);

    // conf.addSeries(new ListSeries("Berlin", 189, 191, 291, 396, 501, 403, 609,
    // 712, 729, 942, 1044, 1247));
    // conf.addSeries(new ListSeries("London", 138, 246, 248, 348, 352, 353, 463,
    // 573, 778, 779, 885, 887));
    // conf.addSeries(new ListSeries("New York", 65, 65, 166, 171, 293, 302, 308,
    // 317, 427, 429, 535, 636));
    // conf.addSeries(new ListSeries("Tokyo", 0, 11, 17, 123, 130, 142, 248, 349,
    // 452, 454, 458, 462));

    // // Add it all together
    // VerticalLayout viewEvents = new VerticalLayout(header, chart);
    // viewEvents.addClassName(Padding.LARGE);
    // viewEvents.setPadding(false);
    // viewEvents.setSpacing(false);
    // viewEvents.getElement().getThemeList().add("spacing-l");
    // return viewEvents;
    // }

    // private Component createServiceHealth() {
    // // Header
    // HorizontalLayout header = createHeader("Service health", "Input / output");

    // // Grid
    // Grid<ServiceHealth> grid = new Grid();
    // grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    // grid.setAllRowsVisible(true);

    // grid.addColumn(new ComponentRenderer<>(serviceHealth -> {
    // Span status = new Span();
    // String statusText = getStatusDisplayName(serviceHealth);
    // status.getElement().setAttribute("aria-label", "Status: " + statusText);
    // status.getElement().setAttribute("title", "Status: " + statusText);
    // status.getElement().getThemeList().add(getStatusTheme(serviceHealth));
    // return status;
    // })).setHeader("").setFlexGrow(0).setAutoWidth(true);
    // grid.addColumn(ServiceHealth::getCity).setHeader("City").setFlexGrow(1);
    // grid.addColumn(ServiceHealth::getInput).setHeader("Input").setAutoWidth(true).setTextAlign(ColumnTextAlign.END);
    // grid.addColumn(ServiceHealth::getOutput).setHeader("Output").setAutoWidth(true)
    // .setTextAlign(ColumnTextAlign.END);

    // grid.setItems(new ServiceHealth(Status.EXCELLENT, "Münster", 324, 1540),
    // new ServiceHealth(Status.OK, "Cluj-Napoca", 311, 1320),
    // new ServiceHealth(Status.FAILING, "Ciudad Victoria", 300, 1219));

    // // Add it all together
    // VerticalLayout serviceHealth = new VerticalLayout(header, grid);
    // serviceHealth.addClassName(Padding.LARGE);
    // serviceHealth.setPadding(false);
    // serviceHealth.setSpacing(false);
    // serviceHealth.getElement().getThemeList().add("spacing-l");
    // return serviceHealth;
    // }

    // private Component createResponseTimes() {
    // HorizontalLayout header = createHeader("Response times", "Average across all
    // systems");

    // // Chart
    // Chart chart = new Chart(ChartType.PIE);
    // Configuration conf = chart.getConfiguration();
    // conf.getChart().setStyledMode(true);
    // chart.setThemeName("gradient");

    // DataSeries series = new DataSeries();
    // series.add(new DataSeriesItem("System 1", 12.5));
    // series.add(new DataSeriesItem("System 2", 12.5));
    // series.add(new DataSeriesItem("System 3", 12.5));
    // series.add(new DataSeriesItem("System 4", 12.5));
    // series.add(new DataSeriesItem("System 5", 12.5));
    // series.add(new DataSeriesItem("System 6", 12.5));
    // conf.addSeries(series);

    // // Add it all together
    // VerticalLayout serviceHealth = new VerticalLayout(header, chart);
    // serviceHealth.addClassName(Padding.LARGE);
    // serviceHealth.setPadding(false);
    // serviceHealth.setSpacing(false);
    // serviceHealth.getElement().getThemeList().add("spacing-l");
    // return serviceHealth;
    // }

    // private HorizontalLayout createHeader(String title, String subtitle) {
    // H2 h2 = new H2(title);
    // h2.addClassNames(FontSize.XLARGE, Margin.NONE);

    // Span span = new Span(subtitle);
    // span.addClassNames(TextColor.SECONDARY, FontSize.XSMALL);

    // VerticalLayout column = new VerticalLayout(h2, span);
    // column.setPadding(false);
    // column.setSpacing(false);

    // HorizontalLayout header = new HorizontalLayout(column);
    // header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
    // header.setSpacing(false);
    // header.setWidthFull();
    // return header;
    // }

    // private String getStatusDisplayName(ServiceHealth serviceHealth) {
    // Status status = serviceHealth.getStatus();
    // if (status == Status.OK) {
    // return "Ok";
    // } else if (status == Status.FAILING) {
    // return "Failing";
    // } else if (status == Status.EXCELLENT) {
    // return "Excellent";
    // } else {
    // return status.toString();
    // }
    // }

    // private String getStatusTheme(ServiceHealth serviceHealth) {
    // Status status = serviceHealth.getStatus();
    // String theme = "badge primary small";
    // if (status == Status.EXCELLENT) {
    // theme += " success";
    // } else if (status == Status.FAILING) {
    // theme += " error";
    // }
    // return theme;
    // }

}
