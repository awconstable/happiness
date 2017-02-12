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

    @RequestMapping("/happiness/{happinessIndex}")
    public Happiness happincess(@PathVariable int happinessIndex)
        {
        return new Happiness(1, happinessIndex);
        }
    }
