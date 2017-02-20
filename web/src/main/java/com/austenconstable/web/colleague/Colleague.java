package com.austenconstable.web.colleague;

import org.springframework.data.annotation.Id;

/**
 * Created by awconstable on 20/02/2017.
 */
public class Colleague
    {

    @Id
    private String id;
    private final String teamId;
    private final String email;

    public Colleague(String teamId, String email)
        {
        this.teamId = teamId;
        this.email = email;
        }

    public String getTeamId()
        {
        return teamId;
        }

    public String getEmail()
        {
        return email;
        }
    }
