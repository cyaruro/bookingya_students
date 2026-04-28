package com.project.bookingya.tdd;

import com.project.bookingya.dtos.ReservationDto;
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
    void testCreateReservation() throws Exception {
        // 1. Obtenemos el huesped y habitación creados en los otros tests
        Guest guest = guestService.getByIdentification("1065896282");
        Room room = roomService.getByCode("101");

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setCheckIn(LocalDateTime.now());
        reservationDto.setCheckOut(LocalDateTime.now().plusDays(4));
        reservationDto.setGuestId(guest.getId());
        reservationDto.setGuestsCount(1);
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
    @Order(2)
    void testGetReservationById() throws Exception {
        Reservation foundReservation = reservationService.getById(savedReservationId);

        assertNotNull(foundReservation);
        assertEquals(savedReservationId, foundReservation.getId());
        assertEquals("Nota creación de reservación", foundReservation.getNotes());
        assertEquals(1, foundReservation.getGuestsCount());
        System.out.println("El Id de la reserva es " + foundReservation.getId());
    }

    @Test
    @Order(3)
    void testUpdateReservation() throws Exception{
        Reservation foundUpdate = reservationService.getById(savedReservationId);
        ReservationDto updateDto = new ReservationDto();
        updateDto.setNotes("Nota de Actualización de la reserva");
        updateDto.setGuestsCount(2);
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
    @Order(4)
    void testDeleteReservation() throws Exception {
        reservationService.delete(savedReservationId);

        assertThrows(EntityNotExistsException.class, () -> {
            reservationService.getById(savedReservationId);
        });
    }
}