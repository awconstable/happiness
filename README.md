# Team Happiness Meter
A proof of concept team happiness meter

Employee happiness is important, this application provides a simple method of capturing happiness ratings over time

[![CircleCI](https://circleci.com/gh/awconstable/happiness.svg?style=shield)](https://circleci.com/gh/awconstable/happiness)
![CodeQL](https://github.com/awconstable/happiness/workflows/CodeQL/badge.svg)
[![codecov](https://codecov.io/gh/awconstable/happiness/branch/master/graph/badge.svg)](https://codecov.io/gh/awconstable/happiness)
[![Libraries.io dependency status for GitHub repo](https://img.shields.io/librariesio/github/awconstable/happiness.svg)](https://libraries.io/github/awconstable/happiness)
[![dockerhub](https://img.shields.io/docker/pulls/awconstable/teamhappiness.svg)](https://cloud.docker.com/repository/docker/awconstable/teamhappiness)

## Limitations

This application is a proof of concept, it is **not** production ready.
A non-exhaustive list of known limitations:
* Ratings can be submitted multiple times per person within a period allowing the results to be intentionally or unintentionally skewed
* No security whatsoever - anonymous users can easily delete or alter all data

## Dependencies

1. MongoDB
2. [Team Service](https://github.com/awconstable/teamservice)

### View happiness trends for the last 12 months

<http://localhost:8080>


---

## Developer guide

### Compile

```
mvn clean install
```

### Run in development

```
#bring up dependencies
docker-compose up
#web
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.data.mongodb.host=<mongo host> --spring.data.mongodb.port=<mongo port> --spring-data.mongodb.database=<mongo db> --spring.cloud.discovery.enabled=true --spring.cloud.service-registry.auto-registration.enabled=true --spring.cloud.consul.discovery.enabled=true --spring.cloud.consul.host=<consul host> --spring.cloud.consul.discovery.prefer-ip-address=true --spring.cloud.consul.port=<consul port> --spring.cloud.consul.config.enabled=true"
#email
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.mail.host=<smtp host> --spring.mail.port=<smtp port> --team.service.url=<team service url>"
```


### Run happiness app

*See https://github.com/docker-library/openjdk/issues/135 as to why spring.boot.mongodb.. env vars don't work*

```
docker stop happiness_app
docker rm happiness_app
docker pull awconstable/teamhappiness
docker run --name happiness_app -d -p 8080:8080 --network mongonetwork -e spring.data.mongodb.host=<mongo host> -e spring.data.mongodb.port=<mongo port> -e spring.data.mongodb.database=<mongo db>  -e spring.cloud.discovery.enabled=true -e spring.cloud.service-registry.auto-registration.enabled=true -e spring.cloud.consul.discovery.enabled=true -e spring.cloud.consul.host=<consul host> -e spring.cloud.consul.discovery.prefer-ip-address=true -e spring.cloud.consul.port=<consul port> -e spring.cloud.consul.config.enabled=true" awconstable/teamhappiness:latest
```

### Run happiness emailer - runs via cron

```
docker run --rm --name happiness_email --network mongonetwork -e spring_data_mongodb_host=<mongo host> -e spring_data_mongodb_port=<mongo port> -e spring_data_mongodb_database=<mongo db> -e rating_url=<rating url> -e view_url=<view url> -e email_subject="How do you feel?" -e from_email=<from email> -e spring_mail_host=<mail host> awconstable/teamhappiness-emailer:latest
```
