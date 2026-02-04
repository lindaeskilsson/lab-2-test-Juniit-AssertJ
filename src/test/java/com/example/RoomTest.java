package com.example;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RoomTest {

    // test on room class.

    //test is room avalible
    @Test
     void returnTrue_ifRoomIsAvalible() {
        Room room = new Room("room-1", "Room-2");

        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);
        LocalDateTime end   = LocalDateTime.of(2026, 1, 2, 0, 0);

        boolean result = room.isAvailable(start, end);
        assertThat(result).isTrue();
    }


}
