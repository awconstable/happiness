package com.austenconstable.email.hierarchy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Repository
public class HierarchyRestRepository
    {

    private RestTemplate restTemplate;

    @Autowired
    public HierarchyRestRepository(RestTemplateBuilder builder)
        {
        this.restTemplate = builder.build();
        }

    @Value("${team.service.url}")
    private String teamServiceUrl;

    public List<HierarchyEntity> findAll()
        {

        ResponseEntity<List<HierarchyEntity>> response = restTemplate.exchange(teamServiceUrl + "/v2/hierarchy/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<HierarchyEntity>>()
                    {
                    });

        return response.getBody();
        }
    }
