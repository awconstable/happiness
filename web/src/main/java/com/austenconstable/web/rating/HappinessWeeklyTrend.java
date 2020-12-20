package com.austenconstable.web.rating;

public class HappinessWeeklyTrend
    {
    private final HappinessTrendId id;
    private final Double avg;
    private final Integer count;
    
    public HappinessWeeklyTrend(HappinessTrendId id, Double avg, Integer count)
        {
        this.id = id;
        this.avg = avg;
        this.count = count;
        }

    public HappinessTrendId getId()
        {
        return id;
        }

    public Double getAvg()
        {
        return avg;
        }

    public Integer getCount()
        {
        return count;
        }

    @Override
    public String toString()
        {
        return "HappinessWeeklyTrend{" +
            "id=" + id +
            ", avg=" + avg +
            ", count=" + count +
            '}';
        }
    }
