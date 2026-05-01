package com.project.bookingya.tdd;

import com.project.bookingya.dtos.RoomDto;
import com.project.bookingya.exceptions.EntityNotExistsException;
import com.project.bookingya.models.Room;
import com.project.bookingya.services.RoomService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class RoomTest {
    @Autowired
    RoomService roomService;

    @Test
    @Order(1)
    void TestCreateRoom() throws Exception {
        RoomDto roomDto = new RoomDto();
        roomDto.setCode("102");
        roomDto.setName("Penhouse");
        roomDto.setCity("Dubai");
        roomDto.setMaxGuests(5);
        roomDto.setNightlyPrice(BigDecimal.valueOf(1000000));
        roomDto.setAvailable(true);
        Room room = roomService.create(roomDto);
        assertNotNull(room);
        assertEquals(room.getCode(), roomDto.getCode());
        assertEquals(room.getName(), roomDto.getName());
        assertEquals(room.getCity(), roomDto.getCity());
    }

    @Test
    @Order(2)
    void testGetRoomByCode() throws Exception{
        String codeBuscada = "102";
        Room room = roomService.getByCode(codeBuscada);
        System.out.println("¡Búsqueda exitosa! Se encontró al habitación: " + room.getCode() + " con ID: " + room.getId());
        assertEquals(codeBuscada, room.getCode());
    }

    @Test
    @Order(3)
    void testGetRoomById() throws Exception{
        String codeBuscada = "102";
        Room roomPorCode = roomService.getByCode(codeBuscada);
        Room roomPorId = roomService.getById(roomPorCode.getId());
        System.out.println("¡Búsqueda exitosa! Se encontró la habitación por Id: " + roomPorId.getCode() + " con ID: " + roomPorId.getId());
        assertEquals(roomPorCode.getId(), roomPorId.getId());
        assertEquals(roomPorCode.getCode(), roomPorId.getCode());
    }

    @Test
    @Order(4)
    void testUpdateRoom() throws Exception{
        String codeBuscada = "102";
        Room room = roomService.getByCode(codeBuscada);
        RoomDto roomDto = new RoomDto();
        roomDto.setName("Suite Ejecutiva");
        Room roomUpdate = roomService.update(roomDto, room.getId());
        assertNotNull(roomUpdate);
        assertEquals(roomDto.getName(), roomUpdate.getName());
    }

    @Test
    @Order(5)
    void testDeleteRoom() throws Exception{
        String codeBuscada = "102";
        Room room = roomService.getByCode(codeBuscada);
        roomService.delete(room.getId());

        // Intentamos buscarlo después de borrado
        assertThrows(EntityNotExistsException.class, () -> {
            roomService.getById(room.getId());
        });
    }
}
