package com.example;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RoomTest {

    // test on room class.

    //test: is room avalible, room without bookings
    @Test
     void returnTrue_ifRoomIsAvalible() {
        Room room = new Room("room-1", "Room-2");

        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);
        LocalDateTime end   = LocalDateTime.of(2026, 1, 2, 0, 0);

        boolean result = room.isAvailable(start, end);
        assertThat(result).isTrue();
    }

    // test: is availble without overlaps
    @Test
    void returnFalse_ifRoomHasOverlappingBooking() {
        Room room = new Room("room-1", "Room-2");

        room.addBooking(booking(
                "booking_1",
                LocalDateTime.of(2026, 1, 1, 0, 0),
                LocalDateTime.of(2026, 1, 3, 0, 0)
        ));

        boolean available = room.isAvailable(
                LocalDateTime.of(2026, 1, 2, 0, 0),
                LocalDateTime.of(2026, 1, 4, 0, 0)
        );

        assertThat(available).isFalse();
    }

    //test add booking and has booking



    //test remove booking


    // test get booking


    //helper for tests
    private Booking booking(String id, LocalDateTime start, LocalDateTime end) {
        return new Booking(
                id,
                "room-1",
                start,
                end
        );
    }

}
