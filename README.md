# Treasure map

## Prerequisites

- JDK 13

## Build project

```
./gradlew build
```
## Run tests

```
./gradlew test
```

## Framework usage

I used Spring Boot 2.2 framework for this app.  
I did not used it in domain packages to be framework agnostic (only Java core allowed)
I mainly used it for :
- testing
- DI (it could be done manually but SB do it well for me) 

## What could be improve

- Treasure quest with multiple users (handle collisions)
- Add additionnal checks on treasure map configuration (a same box should have only one type)
