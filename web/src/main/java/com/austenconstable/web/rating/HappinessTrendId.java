package com.austenconstable.web.rating;

import java.util.Objects;

public class HappinessTrendId
    {
    private final String teamId;
    private final Integer year;
    private final Integer week;

    public HappinessTrendId(String teamId, Integer year, Integer week)
        {
        this.teamId = teamId;
        this.year = year;
        this.week = week;
        }

    public String getTeamId()
        {
        return teamId;
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
    public boolean equals(Object o)
        {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HappinessTrendId that = (HappinessTrendId) o;
        return teamId.equals(that.teamId) &&
            year.equals(that.year) &&
            week.equals(that.week);
        }

    @Override
    public int hashCode()
        {
        return Objects.hash(teamId, year, week);
        }

    @Override
    public String toString()
        {
        return "HappinessTrendId{" +
            "teamId='" + teamId + '\'' +
            ", year=" + year +
            ", week=" + week +
            '}';
        }
    }
