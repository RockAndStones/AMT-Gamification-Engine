Feature: Basic operations for rules

  Background:
    Given there is an Application server

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
