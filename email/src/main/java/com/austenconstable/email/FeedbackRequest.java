package com.austenconstable.email;

/**
 * Created by awconstable on 20/02/2017.
 */
public class FeedbackRequest
    {
        private final String teamId;
        private final String teamName;
        private final String toEmail;
        private final String viewUrl;
        private final String ratingUrl;

    public FeedbackRequest(String teamId, String teamName, String toEmail, String viewUrl, String ratingUrl)
        {
        this.teamId = teamId;
        this.teamName = teamName;
        this.toEmail = toEmail;
        this.viewUrl = viewUrl;
        this.ratingUrl = ratingUrl;
        }

    public String getTeamId()
        {
        return teamId;
        }

    public String getTeamName() { return teamName; }

    public String getToEmail()
        {
        return toEmail;
        }

    public String getViewUrl()
        {
        return viewUrl;
        }

    public String getRatingUrl()
        {
        return ratingUrl;
        }

    @Override
    public String toString()
        {
        return "FeedbackRequest{" +
                "teamId='" + teamId + '\'' +
                ", teamName='" + teamName + '\'' +
                ", toEmail='" + toEmail + '\'' +
                ", viewUrl='" + viewUrl + '\'' +
                ", ratingUrl='" + ratingUrl + '\'' +
                '}';
        }
    }
