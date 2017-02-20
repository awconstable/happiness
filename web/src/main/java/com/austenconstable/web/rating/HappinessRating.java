package com.austenconstable.web.rating;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class HappinessRating
    {

    @Id
    private String id;
    private final String teamId;
    private final Integer happinessRating;
    private final Date ratingDate;

    public HappinessRating(String teamId, Integer happinessRating, Date ratingDate)
        {
        this.teamId = teamId;
        this.happinessRating = happinessRating;
        this.ratingDate = ratingDate;
        }

    public String getTeamId()
        {
        return teamId;
        }

    public Integer getHappinessRating()
        {
        return happinessRating;
        }

    public Date getRatingDate()
        {
        return ratingDate;
        }
    }
