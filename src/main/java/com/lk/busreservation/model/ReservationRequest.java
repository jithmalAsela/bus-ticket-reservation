package com.lk.busreservation.model;

/**
 * @author jithmal
 */
public class ReservationRequest {

    private int passengerCount;
    private String origin;
    private String destination;
    private double priceConfirmation;

    public int getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(int passengerCount) {
        this.passengerCount = passengerCount;
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

    public double getPriceConfirmation() {
        return priceConfirmation;
    }

    public void setPriceConfirmation(double priceConfirmation) {
        this.priceConfirmation = priceConfirmation;
    }
}
