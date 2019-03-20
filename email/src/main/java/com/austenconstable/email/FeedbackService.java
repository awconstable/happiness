package com.austenconstable.email;

import com.austenconstable.email.team.Team;
import com.austenconstable.email.team.TeamMember;
import com.austenconstable.email.team.TeamRestRepository;
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

        private final TeamRestRepository teamRestRepository;
        private final EmailFeedbackRequestSender emailSender;

    @Autowired
    public FeedbackService(TeamRestRepository teamRestRepository, EmailFeedbackRequestSender emailSender)
        {
        this.teamRestRepository = teamRestRepository;
        this.emailSender = emailSender;
        }

    public void processFeedbackRequests(){
            List<Team> teams = teamRestRepository.findAll();

            for (Team team: teams){
                if(team.getTeamMembers() == null){
                    continue;
                }
                for(TeamMember teamMember:team.getTeamMembers())
                    {
                    try
                        {
                            FeedbackRequest request = new FeedbackRequest(team.getSlug(), team.getName(), teamMember.getEmail(), viewUrl, ratingUrl);
                            System.out.println(request);
                            emailSender.requestFeedback(request);
                        } catch (Exception e) {
                            System.out.println("email failed to send for: " + teamMember.getEmail());
                            System.out.println(e);
                        }
                    }
            }
        }

    }
