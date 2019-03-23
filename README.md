# happiness
A proof of concept team happiness meter

Employee happiness is important, this application provides a simple method of capturing happiness ratings over time

## Limitations

This application is a proof of concept, it is **not** production ready.
A non-exhaustive list of known limitations:
* Ratings can be submitted multiple times per person within a period allowing the results to be intentionally or unintentionally skewed
* No security whatsoever - anonymous users can easily delete or alter all data

## Dependencies

1. MongoDB
2. [https://github.com/awconstable/teamservice]

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
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.data.mongodb.host=<mongo host>,--spring.data.mongodb.port=<mongo port>,--spring-data.mongodb.database=<mongo db>"
#email
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.mail.host=<smtp host>,--spring.mail.port=<smtp port>,--team.service.url=<team service url>"
```


### Run happiness app

*See https://github.com/docker-library/openjdk/issues/135 as to why spring.boot.mongodb.. env vars don't work*

```
docker stop happiness_app
docker rm happiness_app
docker run --name happiness_app -d -p 8080:8080 --network mongonetwork -e spring_data_mongodb_host=<mongo host> -e spring_data_mongodb_port=<mongo port> -e spring_data_mongodb_database=<mongo db> awconstable/teamhappiness:latest
```

### Run happiness emailer - runs via cron

```
docker run --rm --name happiness_email --network mongonetwork -e spring_data_mongodb_host=<mongo host> -e spring_data_mongodb_port=<mongo port> -e spring_data_mongodb_database=<mongo db> -e rating_url=<rating url> -e view_url=<view url> -e email_subject="How do you feel?" -e from_email=<from email> -e spring_mail_host=<mail host> awconstable/teamhappiness-emailer:latest
```
