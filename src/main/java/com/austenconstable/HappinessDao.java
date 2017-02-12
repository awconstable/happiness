package com.austenconstable;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by awconstable on 12/02/2017.
 */
@Repository
public class HappinessDao
    {

    private TransportClient client;

    @PostConstruct
    public void initialize() throws UnknownHostException
        {

        client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        }

    @PreDestroy
    public void teardown()
        {
        client.close();
        }

    public void storeHappinessRating(HappinessRating rating) throws Exception
        {
        IndexResponse response = client.prepareIndex("happiness", "rating")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("teamId", rating.getTeamId())
                        .field("ratingDate", new Date())
                        .field("rating", rating.getHappinessRating())
                        .endObject()
                )
                .get();

        }
    }
