package com.austenconstable.web.trend;

import com.austenconstable.web.rating.HappinessRating;
import com.austenconstable.web.rating.HappinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Service
public class TrendService {

    @Autowired
    private HappinessRepository repository;

    public ArrayList<HappinessTrend> getWeeklyTrendData(String team) {
        List<HappinessRating> ratings = getTrendData(team);

        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();

        HashMap<Integer, ArrayList<HappinessRating>> groupedData = groupTrendData(ratings, woy);

        ArrayList<HappinessTrend> monthlyAverage = calculateAverages(team, groupedData, woy);

        return monthlyAverage;
    }

    public ArrayList<HappinessTrend> getMonthlyTrendData(String team) {
        List<HappinessRating> ratings = getTrendData(team);

        TemporalField moy = ChronoField.MONTH_OF_YEAR;

        HashMap<Integer, ArrayList<HappinessRating>> groupedData = groupTrendData(ratings, moy);

        ArrayList<HappinessTrend> monthlyAverage = calculateAverages(team, groupedData, moy);

        return monthlyAverage;
    }

    private List<HappinessRating> getTrendData(String team) {

        LocalDateTime startDate = LocalDateTime.now().minusMonths(12);
        LocalDateTime endDate = LocalDateTime.now();

        List<HappinessRating> ratings = new ArrayList<HappinessRating>();

        if (team == null || "all".equals(team)) {
            ratings = repository.findByRatingDateBetween(startDate, endDate);
        } else {
            ratings = repository.findByTeamIdAndRatingDateBetween(team, startDate, endDate);
        }

        return ratings;
    }

    private Integer getGroupKey(LocalDate date, TemporalField timePeriod){
        return date.get(timePeriod);
    }

    private HashMap<Integer, ArrayList<HappinessRating>> groupTrendData(List<HappinessRating> ratings, TemporalField timePeriod) {

        HashMap<Integer, ArrayList<HappinessRating>> calcstore = new HashMap<>();

        for (HappinessRating rating : ratings) {
            LocalDateTime ratingDate = rating.getRatingDate();
            if (calcstore.containsKey(getGroupKey(ratingDate.toLocalDate(),timePeriod))) {
                calcstore.get(getGroupKey(ratingDate.toLocalDate(),timePeriod)).add(rating);
            } else {
                ArrayList<HappinessRating> list = new ArrayList<HappinessRating>();
                list.add(rating);
                calcstore.put(getGroupKey(ratingDate.toLocalDate(),timePeriod), list);
            }
        }

        return calcstore;

    }

    private ArrayList<HappinessTrend> calculateAverages (String team, HashMap<Integer, ArrayList<HappinessRating>> groupedRatings, TemporalField timePeriod) {

        ArrayList<HappinessTrend> trends = new ArrayList<>();

        Integer periodCount = 0;

        Integer periodSize = Math.toIntExact(timePeriod.range().getSmallestMaximum());

        while(periodCount < periodSize) {

            LocalDate date = LocalDate.now().minus(periodCount, timePeriod.getBaseUnit());

            if (groupedRatings.containsKey(getGroupKey(date,timePeriod))) {
                ArrayList<HappinessRating> ratings1 = groupedRatings.get(getGroupKey(date,timePeriod));
                Integer count = 0;
                Integer total = 0;
                String teamId = "";
                for (HappinessRating r : ratings1) {
                    count++;
                    total = total + r.getHappinessRating();
                    teamId = r.getTeamId();
                }
                Double avg = (double) total / (double) count;

                trends.add(new HappinessTrend(teamId, avg, count, date));
            } else {
                trends.add(new HappinessTrend(team, 0.0, 0, date));
            }
            periodCount++;
        }
        return trends;
    }
}
