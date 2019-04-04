package com.austenconstable.web.rating;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(SpringRunner.class)
@DataMongoTest
public class HappinessRepositoryTest {

    @Autowired
    private HappinessRepository repository;

    @Before
    public void setUp() {
        //TODO this needs setting up properly so that the date remains valid for the 12 month trend window
        List<HappinessRating> ratings = new ArrayList<>();
        ratings.add(new HappinessRating("testid1", 3,
                createDate(2019, Month.JANUARY, 5, 6, 30)));
        ratings.add(new HappinessRating("testid1", 5,
                createDate(2019, Month.JANUARY, 29, 6, 30)));
        ratings.add(new HappinessRating("testid2", 1,
                createDate(2019, Month.FEBRUARY, 18, 6, 30)));
        ratings.add(new HappinessRating("testid2", 3,
                createDate(2019, Month.MARCH, 5, 6, 30)));

        repository.saveAll(ratings);
    }

    @After
    public void clearDown() {
        repository.deleteAll();
    }

    private LocalDateTime createDate(Integer year, Month month, Integer day, Integer hour, Integer minute){
        return LocalDateTime.of(year, month, day, hour, minute);
    }

    @Test
    public void findByTeamIdTest() throws Exception {

        List<HappinessRating> happinessRatings = repository.findByTeamIdIgnoreCase("testid2");

        assertThat(happinessRatings.size()).isEqualTo(2);
    }

    @Test
    public void findAllTest() throws Exception {

        List<HappinessRating> happinessRatings = repository.findAll();

        assertThat(happinessRatings.size()).isEqualTo(4);
    }

    @Test
    public void findByDateRangeTest() throws Exception {

        List<HappinessRating> happinessRatings = repository.findByRatingDateBetween(
                createDate(2019, Month.JANUARY, 1, 4, 30),
                createDate(2019, Month.JANUARY, 30, 22, 30));

        assertThat(happinessRatings.size()).isEqualTo(2);
    }

    @Test
    public void findByTeamIdAndDateRangeTest() throws Exception {

        List<HappinessRating> happinessRatings = repository.findByTeamIdAndRatingDateBetween(
                "testid1",
                createDate(2019, Month.JANUARY, 1, 4, 30),
                createDate(2019, Month.JANUARY, 20, 22, 30));

        assertThat(happinessRatings.size()).isEqualTo(1);
    }

    //TODO WARNING this will fail in the future due to the repository method only looking at the last 12 months
    @Test
    public void getWeeklyChildTrendTest(){

        List<HappinessWeeklyTrend> trends = repository.getWeeklyChildTrend("testid1", new String[] { "testid2"});

        assertThat(trends.size()).isEqualTo(4);
    }
}
