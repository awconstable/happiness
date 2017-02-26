# happiness
A proof of concept team happiness meter

Employee happiness is important to us, this application provides a simple method of capturing happiness ratings over time

## Quick start guide

//TODO add quick start

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
