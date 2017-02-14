package com.austenconstable;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by awconstable on 14/02/2017.
 */
@RepositoryRestResource(collectionResourceRel = "happiness", path = "happiness")
public interface HappinessRepository extends MongoRepository<HappinessRating, String>
    {
           List<HappinessRating> findByTeamId(@Param("teamId") String teamId);
    }
