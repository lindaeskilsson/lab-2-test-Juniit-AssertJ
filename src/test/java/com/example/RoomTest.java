package com.example;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

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
    @Test
    void hasBookingReturnsTrue_afterAddBooking() {
        Room room = new Room("room-1", "Room-2");

        room.addBooking(booking(
                "booking_1",
                LocalDateTime.of(2026, 1, 1, 0, 0),
                LocalDateTime.of(2026, 1, 2, 0, 0)
        ));

        assertThat(room.hasBooking("booking_1")).isTrue();
    }

    // test: has booking when no existing
    @Test
    void hasBookingReturnFalse_whenBookingDoesNotExist() {
        Room room = new Room("room-1", "room-2");

        assertThat(room.hasBooking("missing")).isFalse();
    }

    //test remove booking
    @Test
    void removeBookingRemovesExistingBooking() {
        Room room = new Room("room-1", "room-2");

        room.addBooking(booking(
                "booking-1",
                LocalDateTime.of(2026, 1,1,0,0),
                LocalDateTime.of(2026,1,2,0,0)
        ));

        room.removeBooking("booking-1");
        assertThat(room.hasBooking("booking-1")).isFalse();
    }

    // test get booking return correct booking
    @Test
    void getBookingReturnsBookingWhenIsExist() {
        Room room = new Room("room-1", "room-2");

        Booking booking_1 = booking(
                "booking_1",
                LocalDateTime.of(2026, 1, 1, 0,0),
                LocalDateTime.of(2026, 1, 2, 0,0)
        );
        room.addBooking(booking_1);

        Booking result = room.getBooking("booking_1");
        assertThat(result).isSameAs(booking_1);
    }

    //test: get booking thorws an exeption when it does not exist
@Test
void getBookingThrowsExeptionWhenBookingDoesNotExist(){
        Room room = new Room("room-1", "room-2");
    assertThatThrownBy(()-> room.getBooking("missing"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Bokning finns inte");
}

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
