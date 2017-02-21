package com.austenconstable.web.rating;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;

/**
 * Created by awconstable on 14/02/2017.
 */
@Repository
public class HappinessRepository
    {

    final Logger logger = LoggerFactory.getLogger(HappinessRepository.class);

    @Value("${elasticsearch.host:localhost}")
    private String elasticHost;
    @Value("${elasticsearch.port:9300}")
    private Integer elasticPort;
    private TransportClient client;
    private ObjectMapper jsonMapper;

    @PostConstruct
    public void initElasticConnection() throws Exception
        {
        this.client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(elasticHost), elasticPort));
        logger.info("elasticsearch connection created: " + elasticHost + ":" + elasticPort);
        this.jsonMapper = new ObjectMapper();
        }

    @PreDestroy
    public void closeElasticConnection() throws Exception
        {
        this.client.close();
        logger.info("elasticsearch connection closed");
        }

    public void save(HappinessRating rating) throws Exception
        {

        IndexResponse response = client.prepareIndex("happiness", "rating")
                .setSource(jsonMapper.writeValueAsBytes(rating)
                )
                .get();
        logger.debug("rating stored: " + rating);

        }

    }
