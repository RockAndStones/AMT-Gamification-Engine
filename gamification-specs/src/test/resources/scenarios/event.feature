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