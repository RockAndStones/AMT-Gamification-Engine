Feature: Basic operations for pointscale

  Background:
    Given there is an Application server

  Scenario: create a badge
    Given I have a badge payload
    When I POST the badge payload to the /badges endpoint
    Then I receive a 201 status code

  Scenario: create a pointscale
    Given I have a badge payload
    Given I have a stage payload
    Given I have a pointscale payload
    When I POST the pointscale payload to the /pointscales endpoint
    Then I receive a 201 status code
