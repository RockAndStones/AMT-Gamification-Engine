Feature: Basic operations for rankings
  Background:
    Given there is an Application server

  Scenario: get rankings by badges
    When I send a GET to the /rankings/byBadges endpoint
    Then I receive a 200 status code
    Then I have a paginated badges rankings

  Scenario: get rankings by points
    When I send a GET to the /rankings/byPoints endpoint
    Then I receive a 200 status code
    Then I have a paginated points rankings

