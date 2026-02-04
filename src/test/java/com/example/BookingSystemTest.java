package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

    // test on BookingSystem.Java

    /**
     * Enables Mockito support in JUnit 5 so that @Mock works.
     * Creates mocked dependencies for BookingSystem, allowing
     * tests to run in isolation without database access,
     * notifications, or reliance on system time.
     */

    @ExtendWith(MockitoExtension.class)
    class BookingSystemTest {
        @Mock
        private TimeProvider timeProvider;

        @Mock
        private RoomRepository roomRepository;

        @Mock
        private NotificationService notificationService;

        private BookingSystem bookingSystem;
        private final LocalDateTime now = LocalDateTime.of(2026, 1, 1, 0, 0);

        @BeforeEach
        void setup() {
            bookingSystem = new BookingSystem(timeProvider, roomRepository, notificationService);
            when(timeProvider.getCurrentTime()).thenReturn(now);
        }


        // test on bookRoom:

        // test: verifies that booking is rejected when the start time is in the past
        @Test
        void bookRoomThrows_whenStartTimeIsInThePast() {
            assertThatThrownBy(() -> bookingSystem.bookRoom("room-1",
                    now.minusDays(1),
                    now.plusDays(1)
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Kan inte boka tid i dåtid");
        }


    // test: Verifies that booking is rejected when the end time is before the start time
     @Test
     void bookRoomThrows_whenEndIsBeforeStart() {
         assertThatThrownBy(() -> bookingSystem.bookRoom(
                 "room-1",
                 now.plusDays(2),
                 now.plusDays(1)
         )).isInstanceOf(IllegalArgumentException.class)
                 .hasMessage("Sluttid måste vara efter starttid");
     }

     //test: Verifies that booking fails when the specified room does not exist
        @Test
        void bookRoomThrows_whenSpecifiedRoomDoesNotExist() {
            when(roomRepository.findById("missing")).thenReturn(Optional.empty());

            assertThatThrownBy(()-> bookingSystem.bookRoom(
                    "missing",
                    now.plusDays(1),
                    now.plusDays(2)
                    )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Rummet existerar inte");
        }

        // test: verifies that booking returns false when the room is not avalible
        @Test
        void bookRoomReturnFalse_whenRoomIsNotAvalible() {
            Room room = new Room("room-1", "room-2");
            room.addBooking(new Booking(
                    "booking-1", "room-1",
                    now.plusDays(1),
                    now.plusDays(3)
            ));
            when(roomRepository.findById("room-1")).thenReturn(Optional.of(room));
            boolean result = bookingSystem.bookRoom(
                    "room-1",
                    now.plusDays(2),
                    now.plusDays(4)
            );
        }

        // test:


    // Tests on getAvalibleRooms

    // tests on cancelBooking

   }
