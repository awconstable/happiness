package com.austenconstable.web.trend;

import be.ceau.chart.color.Color;
import be.ceau.chart.data.LineData;
import be.ceau.chart.dataset.LineDataset;
import com.austenconstable.web.rating.HappinessRepository;
import com.austenconstable.web.rating.HappinessWeeklyTrend;
import com.austenconstable.web.team.Team;
import com.austenconstable.web.team.TeamRelation;
import com.austenconstable.web.team.TeamRestRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.visualization.datasource.base.TypeMismatchException;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.TableCell;
import com.google.visualization.datasource.datatable.TableRow;
import com.google.visualization.datasource.datatable.value.DateValue;
import com.google.visualization.datasource.datatable.value.ValueType;
import com.google.visualization.datasource.render.JsonRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;


/**
 * Created by awconstable on 26/02/2017.
 */
@Controller
public class TrendController {

    private final TrendService trendService;

    private final TeamRestRepository teamRepository;

    private final HappinessRepository happinessRepository;

    @Autowired
    public TrendController(TrendService trendService, TeamRestRepository teamRepository, HappinessRepository happinessRepository)
        {
        this.trendService = trendService;
        this.teamRepository = teamRepository;
        this.happinessRepository = happinessRepository;
        }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Deprecated
    @RequestMapping("/trend/monthly/{team}")
    @ResponseBody
    public String rollingMonthlyTrend(Model model, @PathVariable String team) {

        DataTable data = new DataTable();

        data.addColumn(new ColumnDescription("month", ValueType.DATE, "Month"));
        data.addColumn(new ColumnDescription("happiness", ValueType.NUMBER, "Happiness"));
        data.addColumn(new ColumnDescription("responses", ValueType.NUMBER, "Responses"));

        try {
            ArrayList<TableRow> rows = new ArrayList<>();

            ArrayList<HappinessTrend> trends = trendService.getMonthlyTrendData(team);

            for (HappinessTrend trend:trends) {
                TableRow tr = new TableRow();
                tr.addCell(new TableCell(new DateValue(trend.getTrendDate().getYear(), trend.getTrendDate().getMonth().getValue() - 1, 1)
                        , trend.getTrendDate().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)));
                tr.addCell(trend.getAvgHappinessRating());
                tr.addCell(trend.getResponseCount());
                rows.add(tr);
            }

            data.addRows(rows);

        } catch (TypeMismatchException e) {
            System.out.print(e);
        }

        return JsonRenderer.renderDataTable(data, true, true, false).toString();

    }

    @Deprecated
    @RequestMapping("/trend/weekly/{team}")
    @ResponseBody
    public String rollingWeeklyTrend(Model model, @PathVariable String team) {

        DataTable data = new DataTable();

        data.addColumn(new ColumnDescription("week", ValueType.DATE, "Week"));
        data.addColumn(new ColumnDescription("happiness", ValueType.NUMBER, "Happiness"));
        data.addColumn(new ColumnDescription("responses", ValueType.NUMBER, "Responses"));

        try {
            ArrayList<TableRow> rows = new ArrayList<>();

            ArrayList<HappinessTrend> trends = trendService.getWeeklyTrendData(team);

            TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();

            for (HappinessTrend trend:trends) {
                TableRow tr = new TableRow();
                tr.addCell(new TableCell(new DateValue(trend.getTrendDate().getYear(), trend.getTrendDate().getMonth().getValue() - 1, trend.getTrendDate().getDayOfMonth())
                        , "Week " + trend.getTrendDate().get(woy) + ", " + trend.getTrendDate().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                        + " " + trend.getTrendDate().getYear()));
                tr.addCell(trend.getAvgHappinessRating());
                tr.addCell(trend.getResponseCount());
                rows.add(tr);
            }

            data.addRows(rows);

        } catch (TypeMismatchException e) {
            System.out.print(e);
        }

        return JsonRenderer.renderDataTable(data, true, true, false).toString();

    }

    @GetMapping("/")
    public String index(Device device) throws Exception {

        if(device.isMobile() || device.isTablet()){
            return "thankyou";
        }

        return "trendgraph";
    }

    @GetMapping("/chart")
    public String chart() throws Exception {
        return "trendgraph";
    }

    @GetMapping("/thankyou")
    public String thankyou(Device device, @RequestParam(name="team", required = true) String teamId,
                          @RequestParam(name="rating", required = false) String rating, Model model) throws Exception {

        Team team = teamRepository.findByTeamSlug(teamId);

        String teamName = null;
        if(team != null){
            teamName = team.getName();
        }
        model.addAttribute("team", teamName);
        model.addAttribute("rating", rating);

        ArrayList<HappinessTrend> trends = trendService.getWeeklyTrendData(teamId);

        model.addAttribute("trends", trends);

        if(device.isMobile() || device.isTablet()){
            return "thankyou";
        }

        return "trendgraph";
    }

 private  LineDataset createTrendDataSet(String dataSetName, HashMap<String, Double> data, Color colour, boolean child){
     LineDataset dataset = new LineDataset().setLabel(dataSetName);
     data.values().forEach(dataset::addData);
     dataset.setFill(false);
     dataset.setBorderColor(colour);
     dataset.setBorderWidth(2);
     ArrayList<Color> pointsColors = new ArrayList<>();
     pointsColors.add(colour);
     dataset.setPointBackgroundColor(pointsColors);
    if(child)
     {
     dataset.setBorderDash(new ArrayList<Integer>(Arrays.asList(new Integer[]{5, 5})));
     }
     dataset.setYAxisID("y-axis-1");
     return dataset;
 }

private  LineDataset createCountDataSet(String dataSetName, HashMap<String, Integer> data, Color colour, boolean child){
    LineDataset dataset = new LineDataset().setLabel(dataSetName);
    data.values().forEach(dataset::addData);
    dataset.setFill(false);
    dataset.setBorderColor(colour);
    dataset.setBorderWidth(2);
    ArrayList<Color> pointsColors = new ArrayList<>();
    pointsColors.add(colour);
    dataset.setPointBackgroundColor(pointsColors);
    if(child)
        {
        dataset.setBorderDash(new ArrayList<Integer>(Arrays.asList(new Integer[]{5, 5})));
        }
    dataset.setYAxisID("y-axis-2");
    return dataset;
}

@GetMapping("/trend/chartjs/weekly/{teamId}")
@ResponseBody
public ResponseEntity chartHappinessTrend(Model model, @PathVariable String teamId) throws Exception
    {

    ArrayList<String> labels = new ArrayList<>();
    LinkedHashMap<String, Double> teamTrendData = new LinkedHashMap<>();
    LinkedHashMap<String, Integer> teamCountData = new LinkedHashMap<>();
    LinkedHashMap<String, Double> childTrendData = new LinkedHashMap<>();
    LinkedHashMap<String, Integer> childCountData = new LinkedHashMap<>();

    Team team = teamRepository.findByTeamSlug(teamId);

    if(team == null){
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    ArrayList<String> childTeams = new ArrayList<>();

    for (TeamRelation child : team.getChildren())
        {
        childTeams.add(child.getSlug());
        }

    List<HappinessWeeklyTrend> trends = happinessRepository.getWeeklyChildTrend(teamId, childTeams.toArray(new String[]{}));

    for (HappinessWeeklyTrend trend : trends){
        String label = createDataPointLabel(trend.getYear(), trend.getWeek());
        if(!labels.contains(label))
        {
        labels.add(label);
        }
    }

    for(String label:labels){
        teamTrendData.put(label, (double) 0);
        teamCountData.put(label, 0);
        childTrendData.put(label, (double) 0);
        childCountData.put(label, 0);
    }

    for (HappinessWeeklyTrend trend : trends)
        {
        String label = createDataPointLabel(trend.getYear(), trend.getWeek());

        if ("child".equals(trend.getTeamId()))
            {
            childTrendData.put(label, round(trend.getAvg(),2));
            childCountData.put(label, trend.getCount());
            } else
            {
            teamTrendData.put(label, round(trend.getAvg(),2));
            teamCountData.put(label, trend.getCount());
            }
        }

    LineDataset teamTrendDataset = createTrendDataSet(team.getName() + " Happiness", teamTrendData, Color.GOLD, false);
    LineDataset teamCountDataset = createCountDataSet(team.getName() + " Response", teamCountData, Color.GRAY, false);
    LineDataset childTrendDataset = createTrendDataSet("Child Teams Happiness", childTrendData, Color.KHAKI, true);
    LineDataset childCountDataset = createCountDataSet("Child Teams Response", childCountData, Color.LIGHT_GRAY, true);

    LineData data = new LineData()
            .addLabels(labels.toArray(new String[]{}))
            .addDataset(teamTrendDataset)
            .addDataset(teamCountDataset)
            .addDataset(childTrendDataset)
            .addDataset(childCountDataset);

    ObjectWriter writer = new ObjectMapper()
            .writerWithDefaultPrettyPrinter()
            .forType(LineData.class);

    try
        {
        return new ResponseEntity<>(writer.writeValueAsString(data), HttpStatus.OK);
        } catch (JsonProcessingException e)
        {
        throw new RuntimeException(e);
        }
    }

private String createDataPointLabel(int year, int week)
    {

    WeekFields weekFields = WeekFields.of(Locale.getDefault());
    LocalDateTime date = LocalDateTime.now().withYear(year)
            .with(weekFields.weekOfYear(), week + 1)
            .with(weekFields.dayOfWeek(), 7)
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0);

    ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);

    ZonedDateTime zonedDateTime = ZonedDateTime.of(date, ZoneId.of("Europe/London"));

    return zonedDateTime.format(DateTimeFormatter.ISO_INSTANT);
    }


}
