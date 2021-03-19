package com.austenconstable.web.trend;

import com.austenconstable.web.hierarchy.HierarchyClient;
import com.austenconstable.web.rating.HappinessRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TrendController.class)
class TrendControllerTest
    {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrendService mockTrendService;
    @MockBean
    private HierarchyClient mockHierarchyClient;
    @MockBean
    private HappinessRepository mockHappinessRepository; 
    
    @Test
    void get90DayTrend() throws Exception
        {
        String teamId = "id123";
        LocalDateTime date = LocalDateTime.now().toLocalDate().atStartOfDay();
        HappinessTrend trend = new HappinessTrend(teamId, 3.5, 76, date.toLocalDate());        
        when(mockTrendService.get90DayTrend(teamId, date)).thenReturn(trend);
        
        MvcResult result = mockMvc.perform(get("/trend/90days/" + teamId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
        
        String content = result.getResponse().getContentAsString();
        String dateOut = DateTimeFormatter.ISO_LOCAL_DATE.format(date);
        verify(mockTrendService, times(1)).get90DayTrend(teamId, date);
        assertThat(content, is(equalTo("{\"teamId\":\"id123\",\"avgHappinessRating\":3.5,\"responseCount\":76,\"trendDate\":\"" + dateOut + "\"}")));
        }

    @Test
    void testGet90DayTrend() throws Exception
        {
        String teamId = "id123";
        LocalDateTime date = LocalDateTime.of(2021, 3, 20, 0, 0).toLocalDate().atStartOfDay();
        HappinessTrend trend = new HappinessTrend(teamId, 3.5, 76, date.toLocalDate());
        when(mockTrendService.get90DayTrend(teamId, date)).thenReturn(trend);

        MvcResult result = mockMvc.perform(get("/trend/90days/" + teamId + "/2021-03-20")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        verify(mockTrendService, times(1)).get90DayTrend(teamId, date);
        assertThat(content, is(equalTo("{\"teamId\":\"id123\",\"avgHappinessRating\":3.5,\"responseCount\":76,\"trendDate\":\"2021-03-20\"}")));
        }
    }