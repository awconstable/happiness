package com.austenconstable.email;

import com.austenconstable.email.hierarchy.HierarchyEntity;
import com.austenconstable.email.hierarchy.HierarchyRestRepository;
import com.austenconstable.email.hierarchy.Member;
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

        private final HierarchyRestRepository hierarchyRestRepository;
        private final EmailFeedbackRequestSender emailSender;

    @Autowired
    public FeedbackService(HierarchyRestRepository hierarchyRestRepository, EmailFeedbackRequestSender emailSender)
        {
        this.hierarchyRestRepository = hierarchyRestRepository;
        this.emailSender = emailSender;
        }

    void processFeedbackRequests(){
            List<HierarchyEntity> teams = hierarchyRestRepository.findAll();

            for (HierarchyEntity team: teams){
                if(team.getMembers() == null){
                    continue;
                }
                for(Member teamMember:team.getMembers())
                    {
                    try
                        {
                            FeedbackRequest request = new FeedbackRequest(team.getSlug(), team.getName(), teamMember.getEmail(), viewUrl, ratingUrl);
                            System.out.println(request);
                            emailSender.requestFeedback(request);
                        } catch (Exception e) {
                            System.out.println("email failed to send for: " + teamMember.getEmail());
                            System.out.println(e.toString());
                        }
                    }
            }
        }

    }
