package com.austenconstable.web.trend;

import be.ceau.chart.color.Color;
import be.ceau.chart.data.LineData;
import be.ceau.chart.dataset.LineDataset;
import be.ceau.chart.options.elements.Fill;
import com.austenconstable.web.hierarchy.HierarchyClient;
import com.austenconstable.web.hierarchy.HierarchyEntity;
import com.austenconstable.web.hierarchy.Relation;
import com.austenconstable.web.rating.HappinessRating;
import com.austenconstable.web.rating.HappinessRepository;
import com.austenconstable.web.rating.HappinessWeeklyTrend;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;


/**
 * Created by awconstable on 26/02/2017.
 */
@Controller
public class TrendController {

    private final TrendService trendService;

    private final HierarchyClient hierarchyClient;

    private final HappinessRepository happinessRepository;

    @Autowired
    public TrendController(TrendService trendService, HierarchyClient hierarchyClient, HappinessRepository happinessRepository)
        {
        this.trendService = trendService;
        this.hierarchyClient = hierarchyClient;
        this.happinessRepository = happinessRepository;
        }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @GetMapping("/")
    public String index(Device device) {

        if(device.isMobile() || device.isTablet()){
            return "thankyou";
        }

        return "trendgraph";
    }

    @GetMapping("/chart")
    public String chart() {
        return "trendgraph";
    }

    @GetMapping("/thankyou")
    public String thankyou(Device device, @RequestParam(name="team") String teamId,
                          @RequestParam(name="rating", required = false) String rating, Model model) {

        HierarchyEntity team = hierarchyClient.findEntityBySlug(teamId);

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
     dataset.setFill(new Fill(false));
     dataset.setBorderColor(colour);
     dataset.setBorderWidth(2);
     ArrayList<Color> pointsColors = new ArrayList<>();
     pointsColors.add(colour);
     dataset.setPointBackgroundColor(pointsColors);
    if(child)
     {
     dataset.setBorderDash(new ArrayList<Integer>(Arrays.asList(5, 5)));
     }
     dataset.setYAxisID("y-axis-1");
     return dataset;
 }

private  LineDataset createCountDataSet(String dataSetName, HashMap<String, Integer> data, Color colour, boolean child){

    LineDataset dataset = new LineDataset().setLabel(dataSetName);
    data.values().forEach(dataset::addData);
    dataset.setFill(new Fill(false));
    dataset.setBorderColor(colour);
    dataset.setBorderWidth(2);
    ArrayList<Color> pointsColors = new ArrayList<>();
    pointsColors.add(colour);
    dataset.setPointBackgroundColor(pointsColors);
    if(child)
        {
        dataset.setBorderDash(new ArrayList<Integer>(Arrays.asList(5, 5)));
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

    HierarchyEntity team = hierarchyClient.findEntityBySlug(teamId);

    if(team == null){
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    ArrayList<String> childTeams = new ArrayList<>();

    for (Relation child : team.getChildren())
        {
        childTeams.add(child.getSlug());
        }

    List<HappinessWeeklyTrend> trends = happinessRepository.getWeeklyChildTrend(teamId, childTeams.toArray(new String[]{}));

    for (HappinessWeeklyTrend trend : trends){
        String label = createDataPointLabel(trend.getId().getYear(), trend.getId().getWeek());
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
        String label = createDataPointLabel(trend.getId().getYear(), trend.getId().getWeek());

        if ("child".equals(trend.getId().getTeamId()))
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

    @GetMapping("/trend/90days/{teamId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public HappinessTrend get90DayTrend(@PathVariable String teamId){
        return trendService.get90DayTrend(teamId, LocalDateTime.now().toLocalDate().atStartOfDay());
    }

    @GetMapping("/trend/90days/{teamId}/{date}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public HappinessTrend get90DayTrend(@PathVariable String teamId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return trendService.get90DayTrend(teamId, date.atStartOfDay());
    }

    @GetMapping("/ratings")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<HappinessRating> getRatings(){
        return happinessRepository.findAll();
    }

}
