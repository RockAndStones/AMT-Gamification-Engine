Feature: Basic operations for point scale

  Background:
    Given there is an Application server

  Scenario: create a point scale without stages
    Given I have a pointscale payload without stages
    When I POST the pointscale payload to the /pointscales endpoint
    Then I receive a 400 status code

  Scenario: create a point scale with unknown badge in a stage
    Given I have an unknown badge payload
    Given I have a stage payload with an unknown badge
    Given I have a pointscale payload
    When I POST the pointscale payload to the /pointscales endpoint
    Then I receive a 404 status code

  Scenario: create a point scale with a stage that is under 0 points
    Given I have a badge payload
    Given I have a stage payload with negative points
    Given I have a pointscale payload
    When I POST the pointscale payload to the /pointscales endpoint
    Then I receive a 400 status code

  Scenario: create a point scale with a stage containing unusable badge (disabled)
    Given I have a badge payload
    When I POST the badge payload to the /badges endpoint
    Then I receive a 201 status code
    When I send a PUT to the /badge/{name} endpoint with usable at false
    Then I receive a 200 status code
    Given I have a stage payload
    Given I have a pointscale payload
    When I POST the pointscale payload to the /pointscales endpoint
    Then I receive a 404 status code

  Scenario: create a point scale
    Given I have a badge payload
    When I send a PUT to the /badge/{name} endpoint
    Then I receive a 200 status code
    Given I have a stage payload
    Given I have a pointscale payload
    When I POST the pointscale payload to the /pointscales endpoint
    Then I receive a 201 status code

  Scenario: create a point scale with same name (conflict)
    Given I have a badge payload
    Given I have a stage payload
    Given I have a pointscale payload
    When I POST the pointscale payload to the /pointscales endpoint
    Then I receive a 409 status code

  Scenario: get the list of point scale
    When I send a GET to the /pointscales endpoint
    Then I receive a 200 status code

  Scenario: get an unknown point scale id
    Given I have an unknown pointscale id
    When I send a GET to the /pointscales/{id} endpoint
    Then I receive a 404 status code

  Scenario: get a specific point scale
    Given I have a badge payload
    Given I have a stage payload
    Given I have a pointscale payload
    When I send a GET to the /pointscales endpoint
    Then I have a pointscale id
    When I send a GET to the /pointscales/{id} endpoint
    Then I receive a 200 status code
    And I receive a payload that is the same as the previous pointscale payload

  Scenario: remove a point scale
    When I send a GET to the /pointscales endpoint
    Then I have a pointscale id
    When I send DELETE the pointscale id to the /pointscales/{id} endpoint
    Then I receive a 200 status code
    # Remove the stages and the rules realted to the pointscale
