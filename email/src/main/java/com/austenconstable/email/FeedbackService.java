package com.austenconstable.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by awconstable on 20/02/2017.
 */
@Service
public class FeedbackService
    {
        @Value("${view.url}")
        private String viewUrl;
        @Value("${rating.url}")
        private String ratingUrl;

        @Autowired
        private ColleagueRepository colleagueRepository;
        @Autowired
        private EmailFeedbackRequestSender emailSender;

        public void processFeedbackRequests(){
            List<Colleague> colleagues = colleagueRepository.findAll();

            for (Colleague colleague: colleagues){
                 FeedbackRequest request = new FeedbackRequest(colleague.getTeamId(), colleague.getEmail(), viewUrl, ratingUrl);
                 System.out.println(request);
                 emailSender.requestFeedback(request);
            }
        }

    }
