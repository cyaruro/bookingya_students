package com.project.bookingya.atdd;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bookingya.dtos.GuestDto;
import com.project.bookingya.dtos.ReservationDto;
import com.project.bookingya.dtos.RoomDto;

@SpringBootTest
@AutoConfigureMockMvc
public class ReservationAtddTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateReservationSuccessfully() throws Exception {

        // 🔹 1. Crear Guest
        GuestDto guestDto = new GuestDto();
        guestDto.setIdentification("111222333");
        guestDto.setName("Rosa Flor Flores Orquidea");
        guestDto.setEmail("emailrosaflor@gmail.com");

        String guestResponse = mockMvc.perform(post("/guest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guestDto)))
                .andReturn().getResponse().getContentAsString();

        UUID guestId = extractId(guestResponse);

        // 🔹 2. Crear Habitación
        RoomDto roomDto = new RoomDto();
        roomDto.setCode("401"); // 👈 obligatorio
        roomDto.setName("Habitación Doble Premium");
        roomDto.setCity("Bucaramanga");
        roomDto.setMaxGuests(4);
        roomDto.setNightlyPrice((BigDecimal.valueOf(670000)));
        roomDto.setAvailable(true);

        String roomResponse = mockMvc.perform(post("/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomDto)))
                .andReturn().getResponse().getContentAsString();

        UUID roomId = extractId(roomResponse);

        // Creas el traductor
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // 🔹 3. Crear Reservation
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setGuestId(guestId);
        reservationDto.setRoomId(roomId);
        reservationDto.setCheckIn(LocalDateTime.parse("15/05/2026 13:30", formatter));
        reservationDto.setCheckOut(LocalDateTime.parse("17/05/2026 10:30", formatter));
        reservationDto.setGuestsCount(2);
        reservationDto.setNotes("Notas Adicionales Reserva ATDD");

        mockMvc.perform(post("/reservation") // 👈 revisa si es singular
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationDto)))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.guestId").value(guestId.toString()))
                .andExpect(jsonPath("$.roomId").value(roomId.toString()))
                .andExpect(jsonPath("$.checkIn").value("2026-05-15T13:30:00"))
                .andExpect(jsonPath("$.checkOut").value("2026-05-17T10:30:00"))
                .andExpect(jsonPath("$.guestsCount").value(2))
                .andExpect(jsonPath("$.notes").value("Notas Adicionales Reserva ATDD"));

    }
    private UUID extractId(String json) throws Exception {
        return UUID.fromString(objectMapper.readTree(json).get("id").asText()
        );
    }
}