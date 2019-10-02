package com.austenconstable.web.hierarchy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

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

    public HierarchyEntity findEntityBySlug(String slug)
        {
        return restTemplate.getForObject(teamServiceUrl + "/v2/hierarchy/relatives/" + slug, HierarchyEntity.class);
        }

    }
