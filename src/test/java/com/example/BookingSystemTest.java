package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

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
        }


        // test on bookRoom:

        // test: verifies that booking is rejected when the start time is in the past
        @Test
        void bookRoomThrows_whenStartTimeIsInThePast() {
            when(timeProvider.getCurrentTime()).thenReturn(now);

            assertThatThrownBy(() -> bookingSystem.bookRoom(
                    "room-1",
                    now.minusDays(1),
                    now.plusDays(1)
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Kan inte boka tid i dåtid");
        }


    // test: Verifies that booking is rejected when the end time is before the start time
     @Test
     void bookRoomThrows_whenEndIsBeforeStart() {
         when(timeProvider.getCurrentTime()).thenReturn(now);

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
            when(timeProvider.getCurrentTime()).thenReturn(now);

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
            when(timeProvider.getCurrentTime()).thenReturn(now);

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
            assertThat(result).isFalse();
        }

        // test: Verifies that a succesfull booking is done and saved and a notification is sent
        @Test
        void bookRoomReturnsTrue_whenBookingIsDoneAndSendsNotification() throws NotificationException {
            when(timeProvider.getCurrentTime()).thenReturn(now);

            Room room = new Room("room-1", "room-2");
            when(roomRepository.findById("room-1")).thenReturn(Optional.of(room));

            boolean result = bookingSystem.bookRoom(
                    "room-1",
                    now.plusDays(1),
                    now.plusDays(2)
            );

            assertThat(result).isTrue();

            verify(roomRepository).save(room);

            //capture and verify the booking sent to the notification service
            ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
            verify(notificationService).sendBookingConfirmation(captor.capture());

            Booking sentBooking = captor.getValue();
            assertThat(sentBooking.getRoomId()).isEqualTo("room-1");
            assertThat(sentBooking.getStartTime()).isEqualTo(now.plusDays(1));
            assertThat(sentBooking.getEndTime()).isEqualTo(now.plusDays(2));

            assertThat(room.hasBooking(sentBooking.getId())).isTrue();
        }



    // Tests on getAvalibleRooms

        // verfies that an expetions is thrown when start or end time is Null
        @Test
        void getAvalibleRoomsThrows_whenStartorEndIsNull(){
            assertThatThrownBy(() -> bookingSystem.getAvailableRooms(null, now.plusDays(1)))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> bookingSystem.getAvailableRooms(now.plusDays(1), null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        // verifies that en expetions is thrown when end time is before start time
        @Test
        void getAvalibleRoomsThrow_whenEndTimeIsBeforeStartTime() {
            assertThatThrownBy(() -> bookingSystem.getAvailableRooms(now.plusDays(2), now.plusDays(1)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Sluttid måste vara efter starttid");
        }

        // verifies that only avlaible rooms are returned for given time interval
        @Test
        void getAvalibleRoomsReturnOnlyRoomsThatAreAvalible() {
            LocalDateTime start = now.plusDays(2);
            LocalDateTime end = now.plusDays(3);

            Room avalibleRoom = new Room("room-1", "AA");
            Room busyRoom = new Room("room-2", "BB");
            busyRoom.addBooking(new Booking("BB1", "room-2", now.plusDays(1), now.plusDays(4)));

            when(roomRepository.findAll()).thenReturn(List.of(avalibleRoom, busyRoom));
            List<Room> result = bookingSystem.getAvailableRooms(start, end);

            assertThat(result.size()).isEqualTo(1);
            assertThat(result.get(0).getId()).isEqualTo("room-1");
        }

    // tests on cancelBooking

        //Verifies that cancelling a booking with null id throws an exeption
        @Test
        void cancelBookingThrows_whenBookingIdIsNull(){
            assertThatThrownBy(()-> bookingSystem.cancelBooking(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Boknings-id kan inte vara null");
        }

        //Verifiees that cancelling a non-existing booking returns false
        @Test
        void cancelBookingReturnsFalse_whenCancellingNonExistingBooking() throws NotificationException {
            when(roomRepository.findAll()).thenReturn(List.of(
                    new Room("room-1", "AA"),
                    new Room("room-2", "BB")
            ));
            boolean result = bookingSystem.cancelBooking("missing");

            assertThat(result).isFalse();
            verify(roomRepository,never()).save(any());
            verify(notificationService, never()).sendCancellationConfirmation(any());
        }

        //Verifies that cancelling a booking that has started or ended throws expetion
        @Test
        void cancelBookingThrows_whenCancellingAlreadyStartetOrEndedBooking() throws NotificationException {
            when(timeProvider.getCurrentTime()).thenReturn(now);

            Room room = new Room("room-1", "AA");
            room.addBooking(new Booking(
                    "booking-1",
                    "room-1",
                    now.minusDays(1),
                    now.plusDays(1)
            ));

            when(roomRepository.findAll()).thenReturn(List.of(room));

            assertThatThrownBy(() -> bookingSystem.cancelBooking("booking-1"))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Kan inte avboka påbörjad eller avslutad bokning");

            verify(roomRepository, never()).save(any());
            verify(notificationService, never()).sendCancellationConfirmation(any());
        }

        // Verifies that a future booking can be cancelled successfully

        @Test
        void cancelBookingReturnTrue_whenBookingCanBeCancelled() throws NotificationException {
            when(timeProvider.getCurrentTime()).thenReturn(now);
            Room room = new Room ("room-1", "AA");
            Booking booking_1 = new Booking(
                    "booking_1", "room-1",
                    now.plusDays(2),
                    now.plusDays(3)
            );
            room.addBooking(booking_1);

            when(roomRepository.findAll()).thenReturn(List.of(room));

            boolean result = bookingSystem.cancelBooking("booking_1");

            assertThat(result).isTrue();
            assertThat(room.hasBooking("booking_1")).isFalse();
            verify(roomRepository).save(room);

            ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
            verify(notificationService).sendCancellationConfirmation(captor.capture());
            assertThat(captor.getValue().getId()).isEqualTo("booking_1");
        }

        // Verifies that cancellation still succeeds even if notification sending fails
        @Test
        void cancelBookingStillReturnsTrue_whenNotificationFails() throws NotificationException {
            when(timeProvider.getCurrentTime()).thenReturn(now);
            Room room = new Room("room-1", "A");
            room.addBooking(new Booking(
                    "b1", "room-1",
                    now.plusDays(2),
                    now.plusDays(3)
            ));

            when(roomRepository.findAll()).thenReturn(List.of(room));

            doThrow(new NotificationException("fail"))
                    .when(notificationService)
                    .sendCancellationConfirmation(any(Booking.class));

            boolean result = bookingSystem.cancelBooking("b1");

            assertThat(result).isTrue();
            verify(roomRepository).save(room);
            verify(notificationService).sendCancellationConfirmation(any(Booking.class));
        }


    }
