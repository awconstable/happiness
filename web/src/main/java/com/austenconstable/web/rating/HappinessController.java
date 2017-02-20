package com.austenconstable.web.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by awconstable on 12/02/2017.
 */
@RestController
public class HappinessController
    {

    @Autowired
    private HappinessRepository repository;

    @RequestMapping("/happiness/{teamId}/{happinessRating}")
    public HappinessRating happiness(@PathVariable String teamId, @PathVariable int happinessRating) throws Exception
        {

        HappinessRating rating = new HappinessRating(teamId, happinessRating, new Date());
        repository.save(rating);
        return rating;
        }

/*    @RequestMapping(value = "/happiness", method = RequestMethod.GET)
    public Collection<HappinessRating> listRatings()
        {
        return repository.findAll();
        }*/

    @RequestMapping("/happiness/deleteall")
    public String deleteAllData(){
        repository.deleteAll();

        return "deleted all data";
    }

    }
