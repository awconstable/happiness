package com.austenconstable.web.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

public class HappinessAggregationRepositoryImpl implements HappinessAggregationRepository
    {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public HappinessAggregationRepositoryImpl(MongoTemplate mongoTemplate)
        {
        this.mongoTemplate = mongoTemplate;
        }

    @Override
    public List<HappinessWeeklyTrend> getWeeklyChildTrend(String slug, String[] childSlugs)
        {

        ConditionalOperators.Cond condOperation = ConditionalOperators.when(Criteria.where("teamId").is(slug))
                .thenValueOf("teamId")
                .otherwise(LiteralOperators.Literal.asLiteral("child"));

        ProjectionOperation dateProjection = project()
                .and(condOperation).as("teamId")
                .and("happinessRating").as("happinessRating")
                .and("ratingDate").extractYear().as("year")
                .and("ratingDate").extractWeek().as("week");

        GroupOperation groupBy = group("teamId", "year", "week")
                .avg("happinessRating").as("avg")
                .count().as("count");

        LocalDateTime startDate = LocalDateTime.now().minusMonths(12);

        TypedAggregation<HappinessRating> agg = Aggregation.newAggregation(HappinessRating.class,
                match(new Criteria().orOperator(
                        Criteria.where("teamId").in(childSlugs).and("ratingDate").gt(startDate),
                        Criteria.where("teamId").is(slug).and("ratingDate").gt(startDate))),
                dateProjection,
                groupBy,
                sort(Sort.Direction.ASC, "year", "week", "teamId"));

        AggregationResults<HappinessWeeklyTrend> result = mongoTemplate.aggregate(agg, HappinessWeeklyTrend.class);

        return result.getMappedResults();
        }
    }
