Feature: Basic operations for event processor

  Background:
    Given there is an Application server

  Scenario: add points to a user
    Given I have a badge payload
    When I POST the badge payload to the /badges endpoint
    Then I receive a 201 status code
    Given I have a stage payload
    Given I have a pointscale payload
    When I POST the pointscale payload to the /pointscales endpoint
    Then I receive a 201 status code
    When I send a GET to the /pointscales endpoint
    Then I have a pointscale id
    Given I have a rule payload that add 5 points
    Then I POST the rule payload to the /rules endpoint
    Then I receive a 201 status code
    Given I have a user payload
    Given I have a event payload with TestEvent type
    When I POST the event payload to the /events endpoint
    Then I receive a 201 status code
    When I send a GET to the /users/{userAppId} endpoint
    Then I receive a 200 status code
    And user points are equal to 5
    When I send a DELETE to the /badge/{name} endpoint
    Then I receive a 200 status code
    When I send a GET to the /pointscales endpoint
    Then I have a pointscale id
    When I send DELETE the pointscale id to the /pointscales/{id} endpoint
    Then I receive a 200 status code


  Scenario: obtain badge from a stage
