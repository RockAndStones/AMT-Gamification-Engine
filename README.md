# AMT - Gamification Engine <img src="https://github.com/RockAndStones/AMT-Gamification-Engine/workflows/gamification_engine_tests/badge.svg?branch=dev" alt="tests">

## Table of contents
- [Introduction](#Introduction)  
- [Deployment](#Deployment)
    - [Prerequisites](###Deployment)
    - [Deploy the gamification API](#Deploy%20the%20gamification%20API)
- [Cucumber Tests](#Cucumber%20Tests)
    - [Prerequisites](#Cucumber%20Tests)
    - [Run the tests](#Run%20the%20tests)

## Introduction
As part of the course AMT we were asked to create a Spring Boot API to offer a gamification functionality for an application. 

## Deployment
### Prerequisites
To run the tests you need to have installed :
- `git`
- `docker` & `docker-compose`
### Deploy the gamification API
Clone the repository.
```
git clone https://github.com/RockAndStones/AMT-Gamification-Engine.git
```
Open a terminal in the docker folder of the cloned repository and run the following command. 
```
docker-compose up -d
```
You will then be able to access the API at the url http://localhost:8080 when docker-compose is up and running.

## Cucumber Tests
### Prerequisites
To run the tests you need to have installed :
- `git`
- `docker` & `docker-compose`
- `maven`
- `curl`
### Run the tests
To run the tests you need to clone the repository.

```
git clone https://github.com/RockAndStones/AMT-Gamification-Engine.git
```

And to run the `run_cucumber_tests.sh` script from the cloned root folder.
