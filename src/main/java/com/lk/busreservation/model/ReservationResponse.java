package com.lk.busreservation.model;

import java.util.List;

/**
 * @author jithmal
 */
public class ReservationResponse {

    private String ticketNumber;
    private List<String> assignedSeats;
    private String origin;
    private String destination;
    private double totalPrice;

    public ReservationResponse(String ticketNumber, List<String> assignedSeats, String origin, String destination, double totalPrice) {
        this.ticketNumber = ticketNumber;
        this.assignedSeats = assignedSeats;
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

    public List<String> getAssignedSeats() {
        return assignedSeats;
    }

    public void setAssignedSeats(List<String> assignedSeats) {
        this.assignedSeats = assignedSeats;
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
