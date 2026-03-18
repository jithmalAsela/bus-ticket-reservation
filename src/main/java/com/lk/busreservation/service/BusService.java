package com.lk.busreservation.service;

import com.lk.busreservation.config.BusConfig;
import com.lk.busreservation.model.Reservation;
import com.lk.busreservation.model.ReservationResponse;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jithmal
 */
public class BusService {

    private static final BusService INSTANCE = new BusService();
    private final BusConfig config = BusConfig.getInstance();

    private final int totalSeats;
    private final int seatsPerRow;
    private final List<String> seatLabels = new ArrayList<>();
    private final int numSegments;                 // stops count - 1
    private final boolean[][] forwardOccupied;     // [seatIndex][segmentIndex]
    private final boolean[][] returnOccupied;      // [seatIndex][segmentIndex]
    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicInteger ticketCounter = new AtomicInteger(1);

    private BusService() {
        totalSeats = config.getTotalSeats();
        seatsPerRow = config.getSeatsPerRow();
        int numStops = config.getStopOrder().size();
        numSegments = numStops - 1;

        // Generate seat labels
        int numRows = totalSeats / seatsPerRow;
        char lastSeat = (char) ('A' + seatsPerRow - 1);
        for (int row = 1; row <= numRows; row++) {
            for (char seat = 'A'; seat <= lastSeat; seat++) {
                seatLabels.add(row + String.valueOf(seat));
            }
        }

        forwardOccupied = new boolean[totalSeats][numSegments];
        returnOccupied  = new boolean[totalSeats][numSegments];
    }

    public static BusService getInstance() {
        return INSTANCE;
    }

    public synchronized int getAvailableSeats(String origin, String destination) {
        validateRoute(origin, destination);
        String direction = getDirection(origin, destination);
        int[] requiredSegments = getRequiredSegments(origin, destination, direction);
        boolean[][] occupied = direction.equals("forward") ? forwardOccupied : returnOccupied;

        int count = 0;
        for (int seat = 0; seat < totalSeats; seat++) {
            boolean free = true;
            for (int seg : requiredSegments) {
                if (occupied[seat][seg]) {
                    free = false;
                    break;
                }
            }
            if (free) count++;
        }
        return count;
    }

    public synchronized double calculatePrice(String origin, String destination) {
        validateRoute(origin, destination);
        String key = origin.toUpperCase() + "-" + destination.toUpperCase();
        String reverseKey = destination.toUpperCase() + "-" + origin.toUpperCase();
        Map<String, Double> priceMap = config.getPriceMap();

        if (priceMap.containsKey(key)) {
            return priceMap.get(key);
        } else if (priceMap.containsKey(reverseKey)) {
            return priceMap.get(reverseKey);
        } else {
            throw new IllegalArgumentException("No price defined for route: " + origin + " → " + destination);
        }
    }

    public synchronized ReservationResponse reserve(int passengers, String origin,
                                                    String destination, double priceConfirmation) {
        if (passengers <= 0) {
            throw new IllegalArgumentException("Passenger count must be greater than zero.");
        }

        validateRoute(origin, destination);

        double pricePerTicket = calculatePrice(origin, destination);
        double totalPrice = pricePerTicket * passengers;
        if (Math.abs(totalPrice - priceConfirmation) > 0.01) {
            throw new IllegalArgumentException("Price confirmation mismatch. Expected: " + totalPrice);
        }

        String direction = getDirection(origin, destination);
        int[] requiredSegments = getRequiredSegments(origin, destination, direction);
        boolean[][] occupied = direction.equals("forward") ? forwardOccupied : returnOccupied;

        List<Integer> availableIndices = new ArrayList<>();
        for (int seat = 0; seat < totalSeats; seat++) {
            boolean free = true;
            for (int seg : requiredSegments) {
                if (occupied[seat][seg]) {
                    free = false;
                    break;
                }
            }
            if (free) availableIndices.add(seat);
        }

        if (availableIndices.size() < passengers) {
            throw new IllegalStateException("Not enough seats available.");
        }

        List<String> assignedSeatLabels = new ArrayList<>();
        for (int i = 0; i < passengers; i++) {
            int seatIdx = availableIndices.get(i);
            assignedSeatLabels.add(seatLabels.get(seatIdx));
            // Mark segments as occupied
            for (int seg : requiredSegments) {
                occupied[seatIdx][seg] = true;
            }
        }

        String ticketNo = "TICKET-" + ticketCounter.getAndIncrement();
        reservations.add(new Reservation(ticketNo, assignedSeatLabels, origin, destination, totalPrice));

        return new ReservationResponse(ticketNo, assignedSeatLabels, origin, destination, totalPrice);
    }

    private void validateRoute(String origin, String destination) {
        Map<String, Integer> stopOrder = config.getStopOrder();
        String o = origin.toUpperCase();
        String d = destination.toUpperCase();
        if (!stopOrder.containsKey(o) || !stopOrder.containsKey(d) || o.equals(d)) {
            throw new IllegalArgumentException("Invalid route: " + origin + " → " + destination);
        }
    }

    private String getDirection(String origin, String destination) {
        Map<String, Integer> stopOrder = config.getStopOrder();
        int from = stopOrder.get(origin.toUpperCase());
        int to = stopOrder.get(destination.toUpperCase());
        return from < to ? "forward" : "return";
    }

    private int[] getRequiredSegments(String origin, String destination, String direction) {
        Map<String, Integer> stopOrder = config.getStopOrder();
        int o = stopOrder.get(origin.toUpperCase());
        int d = stopOrder.get(destination.toUpperCase());

        if (direction.equals("forward")) {
            int[] segs = new int[d - o];
            for (int i = 0; i < segs.length; i++) segs[i] = o + i;
            return segs;
        } else {
            int[] segs = new int[o - d];
            for (int i = 0; i < segs.length; i++) segs[i] = d + i;
            return segs;
        }
    }
}
