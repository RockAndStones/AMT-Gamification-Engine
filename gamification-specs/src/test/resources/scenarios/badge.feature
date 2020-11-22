Feature: Basic operations for applications

  Background:
    Given there is an Application server

  Scenario: create a badge
    Given I have a badge payload
    When I POST the badge payload to the /badges endpoint
    Then I receive a 201 status code

  Scenario: create a badge already
    Given I have an empty badge payload
    When I POST the badge payload to the /badges endpoint
    Then I receive a 400 status code

  Scenario: create a badge already existing
    Given I have a badge payload
    When I POST the badge payload to the /badges endpoint
    Then I receive a 409 status code

  Scenario: get the list of badges
    When I send a GET to the /badges endpoint
    Then I receive a 200 status code

  Scenario: get the name a desired badge
    Given I have a badge payload
    When I send a GET to the /badge/{name} endpoint
    Then I receive a 200 status code
    And I receive a payload that is the same as the created badge payload

  Scenario: delete a desired badge
    Given I have a badge payload
    When I send a DELETE to the /badge/{name} endpoint
    Then I receive a 200 status code
