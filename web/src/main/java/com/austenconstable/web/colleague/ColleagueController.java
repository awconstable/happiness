package com.austenconstable.web.colleague;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by awconstable on 20/02/2017.
 */
@RestController
public class ColleagueController
    {
    @Autowired
    private ColleagueRepository repository;

    @RequestMapping("/colleague/deleteall")
    public String deleteAllData()
        {
        repository.deleteAll();

        return "deleted all data";
        }

    }
