package com.parkit.parkingsystem.model;

import java.util.Date;

public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;
    private boolean recurringUser = false;

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
		Date retDate;
		retDate = this.inTime;
		return retDate;
    }

    public void setInTime(Date inTimeVal) {
		if (inTimeVal != null)
			this.inTime = new Date(inTimeVal.getTime());
		else
			this.inTime = null;
    }

    public Date getOutTime() {
		Date retDate;
		retDate = this.outTime;
		return retDate;
    }

    public void setOutTime(Date outTimeVal) {
		if (outTimeVal != null)
			this.outTime = new Date(outTimeVal.getTime());
		else
			this.outTime = null;
    }
    
    public boolean getRecurringUser() {
    	return this.recurringUser;
       }
    	
    public void setRecurringUser(boolean recurringUser) {
    	this.recurringUser = recurringUser;
    	       }

}
