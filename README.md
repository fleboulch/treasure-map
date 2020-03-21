[![codecov](https://codecov.io/gh/fleboulch/treasure-map/branch/master/graph/badge.svg)](https://codecov.io/gh/fleboulch/treasure-map)

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

## What could be improved?

- Treasure quest with multiple users has not been implemented
- Add additional checks on treasure map configuration (a same box should have only one type)
- Use a domain id (which is an UUID) for explorer (instead of name)
- Add log message on exceptions

## Performances

I did not do benchmarks on large CSV files but in [ApplicationFactory](src/main/java/com/fleboulch/treasuremap/application/exposition/ApplicationFactory.java) the method `toDomain` use 5 times filter stream method.
This method is in O(n) so performance could be not really good on large collections.  
One solution could be using builder for treasure map construction

## Output

Csv result file is dropped in `output/` folder

## Git history

I did not autosquash my work deliberately in master branch to show the way I work so there is lots of fixme commits
