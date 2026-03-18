package com.lk.busreservation.model;

import java.util.List;

/**
 * @author jithmal
 */
public class Reservation {

    private String ticketNumber;
    private List<String> seats;
    private String origin;
    private String destination;
    private double totalPrice;

    public Reservation(String ticketNumber, List<String> seats, String origin, String destination, double totalPrice) {
        this.ticketNumber = ticketNumber;
        this.seats = seats;
        this.origin = origin;
        this.destination = destination;
        this.totalPrice = totalPrice;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public List<String> getSeats() {
        return seats;
    }

    public void setSeats(List<String> seats) {
        this.seats = seats;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
