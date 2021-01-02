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

  Scenario: get specific event
    Given I have a event payload
    When I POST the event payload to the /events endpoint
    Then I receive a 201 status code
    When I send a GET to the /events endpoint
    Then I receive a 200 status code
    Then I have an event id
    When I send a GET to the /events/{id} endpoint
    Then I receive a 200 status code
    And I receive a payload that is the same as the event payload

  Scenario: get specific event with unknown id
    Given I have an unknown event id
    When I send a GET to the /events/{id} endpoint
    Then I receive a 404 status code

