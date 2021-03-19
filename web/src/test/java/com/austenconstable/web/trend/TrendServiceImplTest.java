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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TrendServiceImplTest
    {

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
    
    @Test
    public void test90DayTrend() {
        String teamId = "id123";
        List<HappinessRating> ratings = new ArrayList<>();
        ratings.add(new HappinessRating(teamId, 1, LocalDateTime.now()));
        ratings.add(new HappinessRating(teamId, 1, LocalDateTime.now()));
        ratings.add(new HappinessRating(teamId, 5, LocalDateTime.now()));
        ratings.add(new HappinessRating(teamId, 5, LocalDateTime.now()));
        ratings.add(new HappinessRating(teamId, 3, LocalDateTime.now()));
        LocalDateTime start = LocalDateTime.now().minusDays(90).toLocalDate().atStartOfDay();
        LocalDateTime end = LocalDateTime.now().toLocalDate().atStartOfDay();
        when(repository.findByTeamIdAndRatingDateBetween(teamId, start, end)).thenReturn(ratings);
        
        HappinessTrend trend = service.get90DayTrend(teamId, end);

        verify(repository, times(1)).findByTeamIdAndRatingDateBetween("id123", start, end);
        assertThat(trend.getAvgHappinessRating(), is(equalTo(3.0)));
        assertThat(trend.getResponseCount(), is(equalTo(5)));
    }


}
