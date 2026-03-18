package com.lk.busreservation.service;

import com.lk.busreservation.model.ReservationResponse;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BusServiceTest {

    private BusService service;

    @Before
    public void setUp() {
        service = BusService.getInstance();
    }

    @Test
    public void testCalculatePrice_Forward() {
        assertEquals(50.0, service.calculatePrice("A", "B"), 0.01);
        assertEquals(100.0, service.calculatePrice("A", "C"), 0.01);
        assertEquals(150.0, service.calculatePrice("A", "D"), 0.01);
    }

    @Test
    public void testCalculatePrice_Return() {
        assertEquals(50.0, service.calculatePrice("B", "A"), 0.01);
        assertEquals(100.0, service.calculatePrice("C", "A"), 0.01);
        assertEquals(150.0, service.calculatePrice("D", "A"), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculatePrice_InvalidRoute() {
        service.calculatePrice("A", "A");
    }

    @Test
    public void testGetAvailableSeats_InitiallyAllFree() {
        assertEquals(40, service.getAvailableSeats("A", "B"));
        assertEquals(40, service.getAvailableSeats("B", "C"));
        assertEquals(40, service.getAvailableSeats("C", "D"));
        assertEquals(40, service.getAvailableSeats("D", "C")); // return
    }

    @Test
    public void testReserveAndCheckAvailability() {
        // Reserve 2 seats from A to C (occupies segments 0 and 1)
        ReservationResponse resp = service.reserve(2, "A", "C", 200.0); // 2*100=200
        assertNotNull(resp.getTicketNumber());
        assertEquals(2, resp.getAssignedSeats().size());

        // Now availability for A->B (only segment 0) should be 38 (40 - 2)
        assertEquals(38, service.getAvailableSeats("A", "B"));

        // Availability for B->C (segment 1) also 38
        assertEquals(38, service.getAvailableSeats("B", "C"));

        // Availability for A->C (segments 0+1) also 38? Actually those 2 seats are occupied on both,
        // so they are not available for A->C. So total 38 free for A->C as well.
        assertEquals(38, service.getAvailableSeats("A", "C"));

        // Reserve 1 seat from B->D (segments 1 and 2)
        resp = service.reserve(1, "B", "D", 100.0); // 1*100=100
        assertEquals(1, resp.getAssignedSeats().size());

        // Now seats occupied on segment 1: 2 (from first) + 1 (from second) = 3
        // So A->B (segment 0) still 38 free
        assertEquals(38, service.getAvailableSeats("A", "B"));
        // B->C (segment 1) free = 40 - 3 = 37
        assertEquals(37, service.getAvailableSeats("B", "C"));
        // C->D (segment 2) free = 40 - 1 = 39
        assertEquals(39, service.getAvailableSeats("C", "D"));
    }

    @Test(expected = IllegalStateException.class)
    public void testReserveNotEnoughSeats() {
        // Try to reserve 41 seats
        service.reserve(41, "A", "B", 2050.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReservePriceMismatch() {
        service.reserve(2, "A", "B", 90.0); // should be 100
    }

}