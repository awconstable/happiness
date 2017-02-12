package com.austenconstable;

public class HappinessRating
    {

    private final long teamId;
    private final int happinessRating;

    public HappinessRating(long teamId, int happinessRating)
        {
        this.teamId = teamId;
        this.happinessRating = happinessRating;
        }

    public long getTeamId()
        {
        return teamId;
        }

    public int getHappinessRating()
        {
        return happinessRating;
        }
    }
