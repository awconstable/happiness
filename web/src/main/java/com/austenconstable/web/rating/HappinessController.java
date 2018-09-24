package com.austenconstable.web.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * Created by awconstable on 12/02/2017.
 */
@RestController
public class HappinessController
    {

    @Autowired
    private HappinessRepository repository;

    @RequestMapping("/happiness/{teamId}/{happinessRating}")
    public void happiness(@PathVariable String teamId, @PathVariable int happinessRating, HttpServletResponse response) throws Exception
        {

        HappinessRating rating = new HappinessRating(teamId, happinessRating, LocalDateTime.now());
            repository.save(rating);
            response.sendRedirect("/thankyou/?team=" + teamId + "&rating=" + happinessRating + "&success=true");
        }

    }