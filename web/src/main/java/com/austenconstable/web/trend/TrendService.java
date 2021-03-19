package com.austenconstable.web.trend;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface TrendService
    {
    ArrayList<HappinessTrend> getWeeklyTrendData(String team);

    ArrayList<HappinessTrend> getMonthlyTrendData(String team);
    
    HappinessTrend get90DayTrend(String team, LocalDateTime date);
    }
