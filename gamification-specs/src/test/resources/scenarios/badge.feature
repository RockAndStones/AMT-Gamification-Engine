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
    And I receive a payload that is the same as the last badge payload

  Scenario: get the name of an unknown badge
    Given I have an unknown badge payload
    When I send a GET to the /badge/{name} endpoint
    Then I receive a 404 status code

  Scenario: Send invalid request to modify badge
    Given I have a wrong badge payload
    When I send a PUT to the /badge/{name} endpoint
    Then I receive a 400 status code

  Scenario: modify a badge with same name
    Given I have a badge payload
    When I send a PUT to the /badge/{name} endpoint
    Then I receive a 200 status code

  Scenario: modify an unknown badge
    Given I have a wrong badge payload
    When I send a PUT to the /badge/{name} endpoint
    Then I receive a 400 status code

  Scenario: delete a badge
    Given I have a badge payload
    When I send a DELETE to the /badge/{name} endpoint
    Then I receive a 200 status code

  Scenario: delete an unknown badge
    Given I have an unknown badge payload
    When I send a DELETE to the /badge/{name} endpoint
    Then I receive a 404 status code

  Scenario: Create, modify and delete a badge
    Given I have a badge payload
    When I POST the badge payload to the /badges endpoint
    Then I receive a 201 status code
    Given I have a modified badge payload
    When I send a PUT to the /badge/{name} endpoint
    Then I receive a 200 status code
    And I receive a payload that is the same as the last badge payload
    When I send a DELETE to the /badge/{name} endpoint
    Then I receive a 200 status code

  #Scenario that lets data in the DB
  Scenario: modify a badge with existing name
    Given I have a badge payload
    When I POST the badge payload to the /badges endpoint
    Then I receive a 201 status code
    Given I have another badge payload
    When I POST the badge payload to the /badges endpoint
    Then I receive a 201 status code
    When I send a PUT to the /badge/{name} endpoint
    Then I receive a 409 status code
    Given I have a badge payload
    When I send a DELETE to the /badge/{name} endpoint
    Then I receive a 200 status code
