package com.austenconstable;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class HappinessRating
    {

    @Id
    private String id;
    private final String teamId;
    private final int happinessRating;
    private final Date ratingDate;

    public HappinessRating(String teamId, int happinessRating, Date ratingDate)
        {
        this.teamId = teamId;
        this.happinessRating = happinessRating;
        this.ratingDate = ratingDate;
        }

    public String getTeamId()
        {
        return teamId;
        }

    public int getHappinessRating()
        {
        return happinessRating;
        }

    public Date getRatingDate()
        {
        return ratingDate;
        }
    }
