package com.austenconstable.web.trend;

import com.austenconstable.web.team.Team;
import com.austenconstable.web.team.TeamRepository;
import com.google.visualization.datasource.base.TypeMismatchException;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.TableCell;
import com.google.visualization.datasource.datatable.TableRow;
import com.google.visualization.datasource.datatable.value.DateValue;
import com.google.visualization.datasource.datatable.value.ValueType;
import com.google.visualization.datasource.render.JsonRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.TextStyle;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;


/**
 * Created by awconstable on 26/02/2017.
 */
@Controller
public class TrendController {

    @Autowired
    private TrendService trendService;

    @Autowired
    private TeamRepository teamRepository;

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
                        , "Week " + Integer.toString(trend.getTrendDate().get(woy)) + ", " + trend.getTrendDate().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
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

    @GetMapping("/thankyou")
    public String thankyou(Device device, @RequestParam(name="team", required = true) String teamId,
                          @RequestParam(name="rating", required = false) String rating, Model model) throws Exception {

        Optional<Team> team = teamRepository.findByTeamIdIgnoreCase(teamId);

        String teamName = team.isPresent() ? team.get().getTeamName() : null;
        model.addAttribute("team", teamName);
        model.addAttribute("rating", rating);

        ArrayList<HappinessTrend> trends = trendService.getWeeklyTrendData(teamId);

        model.addAttribute("trends", trends);

        if(device.isMobile() || device.isTablet()){
            return "thankyou";
        }

        return "trendgraph";
    }

}
