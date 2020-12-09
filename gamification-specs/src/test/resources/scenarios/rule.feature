Feature: Basic operations for rules

  Background:
    Given there is an Application server

  Scenario: create a pointscale for the rule and create a rule
    Given I have a badge payload
    Given I have a stage payload
    Given I have a pointscale payload
    When I POST the pointscale payload to the /pointscales endpoint
    Then I receive a 201 status code
    Given I have a rule payload
    When I POST the rule payload to the /rules endpoint
    Then I receive a 201 status code

  Scenario: create a rule missing important information
    Given I have a rule payload with missing information
    When I POST the rule payload to the /rules endpoint
    Then I receive a 400 status code

  Scenario: create a rule with an unknown badge
    Given I have a rule payload with unknown badge
    When I POST the rule payload to the /rules endpoint
    Then I receive a 404 status code

  Scenario: create a rule with an unknown pointscale
    Given I have a rule payload with an unknown pointscale
    When I POST the rule payload to the /rules endpoint
    Then I receive a 404 status code

  Scenario: get the list of rules
    When I send a GET to the /rules endpoint
    Then I receive a 200 status code

  Scenario: get a specific rule with an id
    Given I have a rule payload
    When I GET the rule payload from the /rules endpoint
    Then I have a rule id
    When I send a GET to the /rules/{id} endpoint
    Then I receive a 200 status code
    And I receive a payload that is the same as the rule payload

  Scenario: get a specific rule with an unknown id
    Given I have an unknown rule id
    When I send a GET to the /rules/{id} endpoint
    Then I receive a 404 status code

  Scenario: remove an unknown rule
    Given I have an unknown rule id
    When I send DELETE the rule id to the /rules/{id} endpoint
    Then I receive a 404 status code

  Scenario: remove a rule
    When I GET the rule payload from the /rules endpoint
    Then I have a rule id
    When I send DELETE the rule id to the /rules/{id} endpoint
    Then I receive a 200 status code
