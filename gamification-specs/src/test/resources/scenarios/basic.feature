Feature: Basic operations for applications

  Background:
    Given there is an Application server

  Scenario: create a event
    Given I have a event payload
    When I POST the event payload to the /events endpoint
    Then I receive a 201 status code

  Scenario: get the list of events
    When I send a GET to the /events endpoint
    Then I receive a 200 status code

  Scenario: create a badge
    Given I have a badge payload
    When I POST the badge payload to the /badges endpoint
    Then I receive a 201 status code

  Scenario: get the list of badges
    When I send a GET to the /badges endpoint
    Then I receive a 200 status code

  Scenario: get the name a desired badge
    Given I have a badge payload
    When I send a GET to the /badge/{name} endpoint
    Then I receive a 200 status code
    And I receive a payload that is the same as the created badge payload
