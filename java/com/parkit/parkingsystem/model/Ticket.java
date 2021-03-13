package com.parkit.parkingsystem.model;

import java.util.Calendar;
import java.util.Date;

public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime = null;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public ParkingSpot getParkingSpot() {
	return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
	this.parkingSpot = parkingSpot;
    }

    public String getVehicleRegNumber() {
	return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
	this.vehicleRegNumber = vehicleRegNumber;
    }

    public double getPrice() {
	return price;
    }

    public void setPrice(double price) {
	this.price = price;
    }

    public Date getInTime() {
	return (new Date(this.inTime.getTime()));
    }

    public void setInTime(Date inTime) {
	this.inTime = new Date(inTime.getTime());
    }

    public Date getOutTime() {
	if (null == outTime)
	    return null;
	else
	    return new Date(this.outTime.getTime());
    }

    public void setOutTime(Date outTime) {
	if (outTime != null)
	    this.outTime = new Date(outTime.getTime());
    }
}
