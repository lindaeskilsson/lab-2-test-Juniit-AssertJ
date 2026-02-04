package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

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
       private final LocalDateTime now = LocalDateTime.of(2026, 1, 1,0,0);

        @BeforeEach
        void setup() {
            bookingSystem = new BookingSystem(timeProvider, roomRepository, notificationService);
            when(timeProvider.getCurrentTime()).thenReturn(now);
        }
    }



    // testd on bookRoom:

    // test:


    // -

    // Tests on getAvalibleRooms

    // tests on cancelBooking


