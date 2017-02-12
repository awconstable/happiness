package com.austenconstable;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by awconstable on 12/02/2017.
 */
@RestController
public class HappinessController
    {

    @RequestMapping("/happiness/{teamId}/{happinessRating}")
    public HappinessRating happiness(@PathVariable long teamId, @PathVariable int happinessRating)
        {
        return new HappinessRating(teamId, happinessRating);
        }
    }
