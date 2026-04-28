Feature: Room Management
  As a booking administrator
  I want to manage room information
  So that I can keep room records up to date

  Scenario: Create a new room
    Given the system does not have a room with code "1001"
    When I create a room with the following details:
      | code | name                      | city        | max_guests | nightly_price | available |
      | 1001 | Suite Altos de Miraflores | Bogotá D.C. | 6          | 2000000       | true      |
    Then the room should be created successfully
    And the room code should be "1001"