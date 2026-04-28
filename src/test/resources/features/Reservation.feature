Feature: Reservation Management
  As a booking administrator
  I want to manage reservations
  So that I can assign guests to rooms

  Scenario: Create a new reservation
    Given the system does not have a guest with identification "49661722"
    And the system does not have a room with code "2001"
    And I create a guest with the following details:
      | identification | name            | email                    |
      | 49661722       | Stella Quintero | stellaquinte@booking.com |
    And I create a room with the following details:
      | code | name                | city              | max_guests | nightly_price | available |
      | 2001 | Suite Azul Profundo | San Andres Island | 10         | 2500000       | true      |
    When I create a reservation with the following details:
      | check_in          | check_out        | identification | guests_count | notes            | code |
      | 2026-05-10 14:00 | 2026-05-15 10:00 | 49661722       | 4            | Vacations Family | 2001 |
    Then the reservation should be created successfully