package com.austenconstable.web.rating;

public class HappinessWeeklyTrend
    {
    
    private final String teamId;
    private final Double avg;
    private final Integer count;
    private final Integer year;
    private final Integer week;

    public HappinessWeeklyTrend(String teamId, Double avg, Integer count, Integer year, Integer week)
        {
        this.teamId = teamId;
        this.avg = avg;
        this.count = count;
        this.year = year;
        this.week = week;
        }

    public String getTeamId()
        {
        return teamId;
        }

    public Double getAvg()
        {
        return avg;
        }

    public Integer getCount()
        {
        return count;
        }

    public Integer getYear()
        {
        return year;
        }

    public Integer getWeek()
        {
        return week;
        }

    @Override
    public String toString()
        {
        return "HappinessWeeklyTrend{" +
                "teamId='" + teamId + '\'' +
                ", avg=" + avg +
                ", count=" + count +
                ", year=" + year +
                ", week=" + week +
                '}';
        }
    }
