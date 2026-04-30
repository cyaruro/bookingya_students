package com.project.bookingya.atdd;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bookingya.dtos.RoomDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RoomAtddTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldCreateRoomSuccessfully() throws Exception{
        RoomDto roomDto = new RoomDto();
        roomDto.setCode("301");
        roomDto.setName("Común sencilla");
        roomDto.setCity("Aguachica");
        roomDto.setMaxGuests(2);
        roomDto.setNightlyPrice(BigDecimal.valueOf(500000.10));
        roomDto.setAvailable(true);

        mockMvc.perform(post("/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.name").value("Común sencilla"))
                .andExpect(jsonPath("$.city").value("Aguachica"))
                .andExpect(jsonPath("$.maxGuests").value(2))
                .andExpect(jsonPath("$.nightlyPrice").value(500000.10))
                .andExpect(jsonPath("$.available").value(true));
    }

}
