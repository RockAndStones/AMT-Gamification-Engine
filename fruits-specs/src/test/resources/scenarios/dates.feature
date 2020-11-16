Feature: Validation of date formats

  Background:
    Given there is an Application server

  Scenario: the expiration date and time are managed
    Given I have a event payload
    When I POST the event payload to the /events endpoint
    Then I receive a 201 status code with a location header
    When I send a GET to the URL in the location header
    Then I receive a 200 status code
    And I receive a payload that is the same as the event payload
