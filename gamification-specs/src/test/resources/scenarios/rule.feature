Feature: Basic operations for rules

  Background:
    Given there is an Application server

  Scenario: create a pointscale for the rule
    Given I have a badge payload
    Given I have a stage payload
    Given I have a pointscale payload
    When I POST the pointscale payload to the /pointscales endpoint
    Then I receive a 201 status code

  Scenario: create a rule
    Given I have a rule payload
    When I POST the rule payload to the /rules endpoint
    Then I receive a 201 status code

  Scenario: create a rule missing important information
    Given I have a rule payload with missing information
    When I POST the rule payload to the /rules endpoint
    Then I receive a 400 status code

  Scenario: create a rule with unknown badge
    Given I have a rule payload with unknown badge
    When I POST the rule payload to the /rules endpoint
    Then I receive a 404 status code

  Scenario: remove a rule
    When I GET the rule payload from the /rules endpoint
    Given I have a rule id
    When I send DELETE the rule id to the /rules/{id} endpoint
    Then I receive a 200 status code
