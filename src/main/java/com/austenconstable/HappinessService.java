package com.austenconstable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by awconstable on 12/02/2017.
 */
@Service
public class HappinessService
    {

    @Autowired
    private HappinessDao happinessDao;

    public void storeHappinessRating(HappinessRating rating) throws Exception
        {
        happinessDao.storeHappinessRating(rating);
        }
    }
