Feature: Guest Management
  As a hotel administrator
  I want to manage guest information
  So that I can keep guest records up to date

  Scenario: Create a new guest
    Given the system does not have a guest with identification "1065885021"
    When I create a guest with the following details:
      | identification | name    | email            |
      | 1065885021     | Diana M | xample@gmail.com |
    Then the guest should be created successfully
    And the guest identification should be "1065885021"