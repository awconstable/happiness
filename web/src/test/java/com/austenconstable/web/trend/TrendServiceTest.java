package com.austenconstable.web.trend;

import com.austenconstable.web.rating.HappinessRating;
import com.austenconstable.web.rating.HappinessRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TrendServiceTest {

    @MockBean
    private HappinessRepository repository;

    @Autowired
    private TrendService service;

    @Test
    public void rollingTrendTest() {
        List<HappinessRating> ratings = new ArrayList<>();
        ratings.add(new HappinessRating("test", 1, LocalDateTime.now()));
        ratings.add(new HappinessRating("test", 4, LocalDateTime.now()));
        given(this.repository.findByRatingDateBetween(isA(LocalDateTime.class),isA(LocalDateTime.class))).willReturn(ratings);

        ArrayList<HappinessTrend> trends = service.getMonthlyTrendData("all");

        assertThat(trends.size(), is(12));

        assertThat(trends.get(0).getAvgHappinessRating(), is(2.5));
    }


}
