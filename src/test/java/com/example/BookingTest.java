package com.example;

import org.junit.jupiter.api.Test;

import java.awt.print.Book;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingTest {

    // checks if no overlap – completely before
    @Test
    void overlapsReturnsFalse_whenOtherIsCompletelyBefore() {
        Booking firstBooking = booking("2026-01-31T00:00", "2026-02-02T00:00");

        LocalDateTime otherStart = LocalDateTime.parse("2026-01-20T00:00");
        LocalDateTime otherEnd   = LocalDateTime.parse("2026-01-25T00:00");

        assertThat(firstBooking.overlaps(otherStart, otherEnd)).isFalse();
    }

    // checks if no overlap - back to back
    @Test
    void overlapsReturnFalse_whenBackToBack() {
        Booking firstBooking = booking(
                "2026-01-10T10:00",
                "2026-01-11T11:00"
        );

        LocalDateTime secondBookingStart =
                LocalDateTime.parse("2026-01-11T11:00");
        LocalDateTime secondBookingEnd =
                LocalDateTime.parse("2026-01-12T11:00");

        assertThat(firstBooking.overlaps(secondBookingStart, secondBookingEnd))
                .isFalse();
    }

    //test if overlap - starts in the middle
    @Test
    void overlapsReturnTrue_whenOtherStartsInTheMiddle(){
        Booking firstBooking = booking("2026-02-03T10:00", "2026-02-03T12:00");

        LocalDateTime secondBookingStart =
                LocalDateTime.parse("2026-02-03T11:00");
        LocalDateTime secondBookingEnd =
                LocalDateTime.parse("2026-02-03T12:00");
    }

    // test if overlap - starts and ends in existing booking
    @Test
    void ovarlapsReturnTrue_whenOtherBookingStartsAndEndsInExistingBooking() {
        Booking firstBooking = booking("2026-02-03T10:00", "2026-02-08T14:00");

        LocalDateTime secondBookingStart =
                LocalDateTime.parse("2026-03-04T10:00");
        LocalDateTime secondBookingEnd =
                LocalDateTime.parse("2026-02-06T14:00");
    }

    //helper for tests
    private Booking booking(String start, String end) {
        return new Booking(
                "1",
                "room-1",
                LocalDateTime.parse(start),
                LocalDateTime.parse(end)
        );
    }
}
