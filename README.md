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

## Quick start guide

### Add a team

```
 curl -H "Content-Type: application/json" -X POST -d '{"teamId": "esrp", "teamName": "Team Name", "platformName": "Platform Name", "domainName": "Domain Name"}' http://localhost:8080/team
```

### Add team members

```
curl -H "Content-Type: application/json" -X POST -d '{"teamId": "teamname", "email": "user.email@big.co"}' http://localhost:8080/colleague
```

### Update team members
```
curl -H "Content-Type: application/json" -X PUT -d '{"teamId": "teamname", "email": "user.email@big.co"}' http://localhost/colleague/{itemid}
```
### View team members

<http://localhost:8080/colleague>

### Delete a team member

```
curl -X DELETE http://localhost:8080/colleague/{id}
```

### View happiness trends for the last 12 months

<http://localhost:8080>


---

## Developer guide

### Compile

```
mvn clean install
```

### Run in development

*Might be **-Drun.arguments** - see: https://stackoverflow.com/questions/23316843/get-command-line-arguments-from-spring-bootrun*

```
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.data.mongodb.host=<mongo host>,--spring.data.mongodb.port=<mongo port>,--spring-data.mongodb.database=<mongo db>"
```

### Create Docker containers

```
mvn dockerfile:build dockerfile:tag
```

### Deploy web docker image from Vagrant to live

```
docker save team/happiness | bzip2 | ssh <destination host> 'bunzip2 | docker load'
```

### Deploy email docker image from Vagrant to live

```
docker save team/happiness-email | bzip2 | ssh <destination host> 'bunzip2 | docker load'
```

### Run happiness app

*See https://github.com/docker-library/openjdk/issues/135 as to why spring.boot.mongodb.. env vars don't work*

```
docker stop happiness_app
docker rm happiness_app
docker run --name happiness_app -d -p 8080:8080 --network mongonetwork -e spring_data_mongodb_host=<mongo host> -e spring_data_mongodb_port=<mongo port> -e spring_data_mongodb_database=<mongo db> team/happiness:0.0.1-SNAPSHOT
```

### Run happiness emailer - runs via cron

```
docker run --rm --name happiness_email --network mongonetwork -e spring_data_mongodb_host=<mongo host> -e spring_data_mongodb_port=<mongo port> -e spring_data_mongodb_database=<mongo db> -e rating_url=<rating url> -e view_url=<view url> -e email_subject="How do you feel?" -e from_email=<from email> -e spring_mail_host=<mail host> team/happiness-email:0.0.1-SNAPSHOT
```
