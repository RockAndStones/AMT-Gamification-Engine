# AMT - Gamification Engine <img src="https://github.com/RockAndStones/AMT-Gamification-Engine/workflows/gamification_engine_tests/badge.svg?branch=dev" alt="tests">

## Table of contents
- [Introduction](#introduction)  
- [Deployment](#deployment)
    - [Prerequisites](#prerequisites)
    - [Deploy the gamification API](#deploy-the-gamification-API)
- [Cucumber Tests](#cucumber-tests)
    - [Prerequisites](#prerequisites-1)
    - [Run the tests](#run-the-tests)
- [Load Tests](#load-tests)
    - [Prerequisites](#prerequisites-2)
    - [Run the tests](#run-the-tests-1)

## Introduction
As part of the course AMT we were asked to create a Spring Boot API to offer a gamification functionality for an application. In this project we used Cucumber and JMeter to test our API.

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
With this tests we can check that the all the endpoints are working as wanted by checking the http codes returned and the different payloads returned in some cases. All the different actions available on the endpoints (GET, POST, PUT, DELETE) were tested.
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

Remark : To run the tests you must have an instance of the gamification-engine running.

## Load Tests
With these tests we check that we have no concurrency problems when handling events or when creating multiple badges, pointscales or even rules.
### Prerequisites
To run the tests you need to have installed :
- `git`
- `JMeter`
### Run the tests
To run the tests you need to clone the repository.

```
git clone https://github.com/RockAndStones/AMT-Gamification-Engine.git
```

And then open the JMeter application and open the file `load-tests.jmx` in the `load-tests` folder.

Remark : To run the tests you must have an instance of the gamification-engine running.
