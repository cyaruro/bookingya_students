package com.project.bookingya.tdd;

import com.project.bookingya.dtos.GuestDto;
import com.project.bookingya.dtos.ReservationDto;
import com.project.bookingya.dtos.RoomDto;
import com.project.bookingya.exceptions.EntityNotExistsException;
import com.project.bookingya.models.Guest;
import com.project.bookingya.models.Reservation;
import com.project.bookingya.models.Room;
import com.project.bookingya.services.GuestService;
import com.project.bookingya.services.ReservationService;
import com.project.bookingya.services.RoomService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservationTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private GuestService guestService;

    // Usamos esta variable estática para mantener el ID de la reservación entre tests
    private static UUID savedReservationId;

    @Test
    @Order(1)
    void TestCreateGuest() throws Exception{
        GuestDto guestDto = new GuestDto();
        guestDto.setIdentification("1010101010");
        guestDto.setName("Pepito Huesped para Reserva");
        guestDto.setEmail("huespedparareservacione@mailxample.com");
        Guest guest = guestService.create(guestDto);
        assertNotNull(guest);
        assertEquals(guest.getIdentification(), guestDto.getIdentification());
        assertEquals(guest.getName(), guestDto.getName());
        assertEquals(guest.getEmail(), guestDto.getEmail());
    }

    @Test
    @Order(2)
    void TestCreateRoom() throws Exception {
        RoomDto roomDto = new RoomDto();
        roomDto.setCode("1002");
        roomDto.setName("Penhouse para Reservación");
        roomDto.setCity("Dubai - Reservación");
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
    @Order(3)
    void testCreateReservation() throws Exception {
        // 1. Obtenemos el huesped y habitación creados en los test anteriores
        Guest guest = guestService.getByIdentification("1010101010");
        Room room = roomService.getByCode("1002");

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setCheckIn(LocalDateTime.now());
        reservationDto.setCheckOut(LocalDateTime.now().plusDays(4));
        reservationDto.setGuestId(guest.getId());
        reservationDto.setGuestsCount(2);
        reservationDto.setNotes("Nota creación de reservación");
        reservationDto.setRoomId(room.getId());

        Reservation reservation = reservationService.create(reservationDto);

        assertNotNull(reservation);
        assertNotNull(reservation.getId());
        assertEquals(reservationDto.getCheckIn(), reservation.getCheckIn());
        assertEquals(reservationDto.getCheckOut(), reservation.getCheckOut());
        assertEquals(reservationDto.getGuestId(), reservation.getGuestId());
        assertEquals(reservationDto.getGuestsCount(), reservation.getGuestsCount());
        assertEquals(reservationDto.getNotes(), reservation.getNotes());
        assertEquals(reservationDto.getRoomId(), reservation.getRoomId());

        savedReservationId = reservation.getId();
    }

    @Test
    @Order(4)
    void testGetReservationById() throws Exception {
        Reservation foundReservation = reservationService.getById(savedReservationId);

        assertNotNull(foundReservation);
        assertEquals(savedReservationId, foundReservation.getId());
        assertEquals("Nota creación de reservación", foundReservation.getNotes());
        assertEquals(2, foundReservation.getGuestsCount());
        System.out.println("El Id de la reserva es " + foundReservation.getId());
    }

    @Test
    @Order(5)
    void testUpdateReservation() throws Exception{
        Reservation foundUpdate = reservationService.getById(savedReservationId);
        ReservationDto updateDto = new ReservationDto();
        updateDto.setNotes("Nota de Actualización de la reserva");
        updateDto.setGuestsCount(3);
        updateDto.setCheckIn(foundUpdate.getCheckIn());
        updateDto.setCheckOut(foundUpdate.getCheckOut());
        updateDto.setRoomId(foundUpdate.getRoomId());
        updateDto.setGuestId(foundUpdate.getGuestId());

        Reservation updateReservation = reservationService.update(updateDto, savedReservationId);

        assertNotNull(updateDto);
        assertEquals(savedReservationId, updateReservation.getId());
        assertEquals(updateDto.getNotes(), updateReservation.getNotes());
        assertEquals(updateDto.getGuestsCount(), updateReservation.getGuestsCount());
        assertEquals(updateDto.getCheckIn(), updateReservation.getCheckIn());
        assertEquals(updateDto.getCheckOut(), updateReservation.getCheckOut());
        assertEquals(updateDto.getRoomId(), updateReservation.getRoomId());
        assertEquals(updateDto.getGuestId(), updateReservation.getGuestId());
    }

    @Test
    @Order(6)
    void testDeleteReservation() throws Exception {
        reservationService.delete(savedReservationId);

        assertThrows(EntityNotExistsException.class, () -> {
            reservationService.getById(savedReservationId);
        });
    }
}