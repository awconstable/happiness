package com.austenconstable.web.rating;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ExtendWith(SpringExtension.class)
@DataMongoTest
public class HappinessRepositoryTest {

    @Autowired
    private HappinessRepository repository;

    private Year year = Year.now();

    @BeforeEach
    public void setUp() {
        List<HappinessRating> ratings = new ArrayList<>();
        ratings.add(new HappinessRating("testid1", 3,
                createDate(year.getValue(), Month.JANUARY, 5, 6, 30)));
        ratings.add(new HappinessRating("testid1", 5,
                createDate(year.getValue(), Month.JANUARY, 29, 6, 30)));
        ratings.add(new HappinessRating("testid2", 1,
                createDate(year.getValue(), Month.FEBRUARY, 18, 6, 30)));
        ratings.add(new HappinessRating("testid2", 3,
                createDate(year.getValue(), Month.MARCH, 5, 6, 30)));

        repository.saveAll(ratings);
    }

    @AfterEach
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
                createDate(year.getValue(), Month.JANUARY, 1, 4, 30),
                createDate(year.getValue(), Month.JANUARY, 30, 22, 30));

        assertThat(happinessRatings.size()).isEqualTo(2);
    }

    @Test
    public void findByTeamIdAndDateRangeTest() throws Exception {

        List<HappinessRating> happinessRatings = repository.findByTeamIdAndRatingDateBetween(
                "testid1",
                createDate(year.getValue(), Month.JANUARY, 1, 4, 30),
                createDate(year.getValue(), Month.JANUARY, 20, 22, 30));

        assertThat(happinessRatings.size()).isEqualTo(1);
    }

    @Test
    public void getWeeklyChildTrendTest(){

        List<HappinessWeeklyTrend> trends = repository.getWeeklyChildTrend("testid1", new String[] { "testid2"});

        assertThat(trends.size()).isEqualTo(4);
    }
}
