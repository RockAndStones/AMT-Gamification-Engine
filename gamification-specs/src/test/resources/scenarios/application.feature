Feature: Basic operations for applications

  Background:
    Given there is an Application server

  Scenario: create an application
    Given I have an application payload
    When I POST the application payload to the /application endpoint
    Then I receive a 201 status code
