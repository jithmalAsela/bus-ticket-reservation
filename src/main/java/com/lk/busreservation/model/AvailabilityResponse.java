package com.lk.busreservation.model;

/**
 * @author jithmal
 */
public class AvailabilityResponse {

    private int availableSeats;
    private double totalPrice;

    public AvailabilityResponse(int availableSeats, double totalPrice) {
        this.availableSeats = availableSeats;
        this.totalPrice = totalPrice;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
