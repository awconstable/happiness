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
2. Elasticsearch & Kibana 5.2

## Quick start guide

//TODO add quick start
1. [Vagrant based Elasticsearch & Kibana 5.2](https://github.com/awconstable/elasticsearch) or your own install.

### Add team members

```
curl -H "Content-Type: application/json" -X POST -d '{"teamId": "teamname", "email": "user.email@big.co"}' http://localhost:8080/colleague
```

### View team members

<http://localhost:8080/colleague>

### Delete a team member

```
curl -X DELETE http://localhost:8080/colleague/{id}
```

### View happiness trends for the last 6 months

<http://localhost:8080>