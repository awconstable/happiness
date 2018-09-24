package com.austenconstable.web.rating;

import org.springframework.data.annotation.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class HappinessRating
    {
    @Id
    private String id;
    private final String teamId;
    private final Integer happinessRating;
    private final LocalDateTime ratingDate;

    public HappinessRating(String teamId, Integer happinessRating, LocalDateTime ratingDate)
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

    public LocalDateTime getRatingDate()
        {
        return ratingDate;
        }

    @Override
    public String toString()
        {
        return "HappinessRating{" +
                "teamId='" + teamId + '\'' +
                ", happinessRating=" + happinessRating +
                ", ratingDate=" + ratingDate +
                '}';
        }
    }
