package com.austenconstable.web.rating;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by awconstable on 13/12/2017.
 */
@RepositoryRestResource(collectionResourceRel = "happiness", path = "happiness")
public interface HappinessRepository extends MongoRepository<HappinessRating, String>, HappinessAggregationRepository
    {
        List<HappinessRating> findByTeamIdIgnoreCase(String teamId);

        List<HappinessRating> findByRatingDateBetween(LocalDateTime startDate, LocalDateTime endDate);

        List<HappinessRating> findByTeamIdAndRatingDateBetween(String teamId, LocalDateTime startDate, LocalDateTime endDate);

    }
