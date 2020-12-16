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
    Given I have a event payload with "TestEvent" type
    When I POST the event payload to the /events endpoint
    Then I receive a 201 status code
    When I send a GET to the /users/{userAppId} endpoint
    Then I receive a 200 status code
    And user points are equal to 5

  Scenario: trying to win deactivated badge
    Given I have a badge payload
    When I send a PUT to the /badge/{name} endpoint with usable at false
    Then I receive a 200 status code
    Given I have a user payload
    Given I have a event payload with "TestEvent" type
    When I POST the event payload to the /events endpoint
    Then I receive a 201 status code
    When I send a GET to the /users/{userAppId} endpoint
    Then I receive a 200 status code
    And user points are equal to 10
    And user badges is empty
    When I send a PUT to the /badge/{name} endpoint with usable at true
    Then I receive a 200 status code

  Scenario: obtain badge from a rule
    Given I have a badge payload
    Given I have a user payload
    When I send a GET to the /pointscales endpoint
    Then I have a pointscale id
    Given I have a rule payload that add 0 points and a badge
    Then I POST the rule payload to the /rules endpoint
    Then I receive a 201 status code
    Given I have a event payload with "EventForABeautifulBadge" type
    When I POST the event payload to the /events endpoint
    Then I receive a 201 status code
    When I send a GET to the /users/{userAppId} endpoint
    Then I receive a 200 status code
    # Points are kept from the last scenario and the rule does not add new points
    And user points are equal to 10
    And user badges last badge is equal to the badge payload
    When I GET the rule payload from the /rules endpoint
    Then I have a rule id
    When I send DELETE the rule id to the /rules/{id} endpoint
    Then I receive a 200 status code


  Scenario: obtain badge from a stage
    Given I have a badge payload
    Given I have a user payload
    Given I have a event payload with "TestEvent" type
    When I POST the event payload to the /events endpoint
    Then I receive a 201 status code
    When I send a GET to the /users/{userAppId} endpoint
    Then I receive a 200 status code
    And user points are equal to 15
    And user badges last badge is equal to the badge payload
    When I send a DELETE to the /badge/{name} endpoint
    Then I receive a 200 status code
    When I send a GET to the /pointscales endpoint
    Then I have a pointscale id
    When I send DELETE the pointscale id to the /pointscales/{id} endpoint
    Then I receive a 200 status code
