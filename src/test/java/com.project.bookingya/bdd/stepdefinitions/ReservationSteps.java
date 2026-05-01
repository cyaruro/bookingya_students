package com.project.bookingya.bdd.stepdefinitions;

import com.project.bookingya.dtos.ReservationDto;
import com.project.bookingya.models.Guest;
import com.project.bookingya.models.Reservation;
import com.project.bookingya.models.Room;
import com.project.bookingya.services.GuestService;
import com.project.bookingya.services.ReservationService;
import com.project.bookingya.services.RoomService;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class  ReservationSteps {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private GuestService guestService;

    private Reservation reservation;
    private Room room;
    private Guest guest;

    @When("I create a reservation with the following details:")
    public void createReservation(io.cucumber.datatable.DataTable dataTable){
        List<Map<String, String>> rows = dataTable.asMaps();
        Map<String, String> row = rows.get(0);

        guest = guestService.getByIdentification(row.get("identification"));
        room = roomService.getByCode(row.get("code"));

        // Definimos el patrón exacto que usamos en la tabla de Gherkin
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setCheckIn(LocalDateTime.parse(row.get("check_in"), formatter));
        reservationDto.setCheckOut(LocalDateTime.parse(row.get("check_out"), formatter));
        reservationDto.setGuestId(guest.getId());
        reservationDto.setGuestsCount(Integer.valueOf(row.get("guests_count")));
        reservationDto.setNotes(row.get("notes"));
        reservationDto.setRoomId(room.getId());

        reservation = reservationService.create(reservationDto);
    }

    @Then("the reservation should be created successfully")
    public void reservationCreatedSuccessfully() {
        assertNotNull(reservation);
    }
}
