package com.austenconstable.web.rating;

import java.util.List;

public interface HappinessAggregationRepository
    {

    List<HappinessWeeklyTrend> getWeeklyChildTrend(String slug, String[] childSlugs);

    }
