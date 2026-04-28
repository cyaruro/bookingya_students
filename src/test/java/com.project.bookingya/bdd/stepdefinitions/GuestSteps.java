package com.project.bookingya.bdd.stepdefinitions;

import com.project.bookingya.dtos.GuestDto;
import com.project.bookingya.models.Guest;
import com.project.bookingya.services.GuestService;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import io.cucumber.java.en.Given;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class GuestSteps {

    @Autowired
    private GuestService guestService;

    private Guest guest;

    @Given("the system does not have a guest with identification {string}")
    public void systemDoesNotHaveGuest(String identification) {
        try {
            guestService.getByIdentification(identification);
        } catch (Exception e) {
            // Guest does not exist, which is expected
        }
    }

    @When("I create a guest with the following details:")
    public void createGuest(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        Map<String, String> row = rows.get(0);
        GuestDto guestDto = new GuestDto();
        guestDto.setIdentification(row.get("identification"));
        guestDto.setName(row.get("name"));
        guestDto.setEmail(row.get("email"));

        guest = guestService.create(guestDto);
    }

    @Then("the guest should be created successfully")
    public void guestCreatedSuccessfully() {
        assertNotNull(guest);
    }

    @Then("the guest identification should be {string}")
    public void guestIdentificationShouldBe(String identification) {
        assertEquals(identification, guest.getIdentification());
    }
}