package com.project.bookingya.bdd.stepdefinitions;

import com.project.bookingya.dtos.RoomDto;
import com.project.bookingya.models.Room;
import com.project.bookingya.services.RoomService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RoomSteps {

    @Autowired
    private RoomService roomService;

    private Room room;

    @Given("the system does not have a room with code {string}")
    public void systemDoesNotHaveRoom(String code) {
        try {
            roomService.getByCode(code);
        } catch (Exception e) {
            // Room does not exist, which is expected
        }
    }

    @When("I create a room with the following details:")
    public void createRoom(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        Map<String, String> row = rows.get(0);
        RoomDto roomDto = new RoomDto();
        roomDto.setCode(row.get("code"));
        roomDto.setName(row.get("name"));
        roomDto.setCity(row.get("city"));
        roomDto.setMaxGuests(Integer.valueOf(row.get("max_guests")));
        roomDto.setAvailable(Boolean.parseBoolean(row.get("available")));
        roomDto.setNightlyPrice(BigDecimal.valueOf(Long.parseLong(row.get("nightly_price"))));

        room = roomService.create(roomDto);
    }

    @Then("the room should be created successfully")
    public void roomCreatedSuccessfully() {
        assertNotNull(room);
    }

    @Then("the room code should be {string}")
    public void roomCodeShouldBe(String code) {
        assertEquals(code, room.getCode());
    }

}
