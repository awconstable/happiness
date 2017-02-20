package com.austenconstable.email;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by awconstable on 20/02/2017.
 */
@Repository
public interface ColleagueRepository extends MongoRepository<Colleague, String>
    {
    List<Colleague> findByTeamId(@Param("teamId") String teamId);
    }
