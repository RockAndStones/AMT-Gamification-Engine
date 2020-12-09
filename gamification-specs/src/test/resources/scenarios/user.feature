Feature: Basic operations for users

  Background:
    Given there is an Application server

  Scenario: create a user through an event and can get the new user with his userAppId
    Given I have a event payload and a user
    When I POST the event payload to the /events endpoint
    Then I receive a 201 status code
    When I send a GET to the /users/{userAppId} endpoint
    Then I receive a user payload with the same {userAppId}

  Scenario: get the list of users
    When I send a GET to the /users endpoint
    Then I receive a 200 status code

  Scenario: get an unknown user
    Given I have an unknown user
    When I send a GET to the /users/{userAppId} endpoint
    Then I receive a 404 status code

