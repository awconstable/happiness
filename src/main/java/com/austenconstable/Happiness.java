package com.austenconstable;

public class Happiness
    {

    private final long teamId;
    private final int happinessIndex;

    public Happiness(long teamId, int happinessIndex)
        {
        this.teamId = teamId;
        this.happinessIndex = happinessIndex;
        }

    public long getTeamId()
        {
        return teamId;
        }

    public int getHappinessIndex()
        {
        return happinessIndex;
        }
    }
