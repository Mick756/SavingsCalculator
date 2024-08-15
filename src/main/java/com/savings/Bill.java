package com.savings;

import org.joda.time.*;
import org.json.JSONObject;

public class Bill {

	public final String name;
	public final int dayDue;
	public final double amount;
	
	private double amountPaid;
	private boolean paid;
	
	public Bill(String name, int dayDue, double amount) {
		this.name = name;
		this.dayDue = dayDue;
		this.amount = amount;
	}
	
	public Bill(String name, LocalDate firstDate, double amount) {
		this.name = name;
		this.dayDue = firstDate.getDayOfMonth();
		this.amount = amount;
	}
	
	
	/**
	 * Check if the bill is paid in full
	 * @return if the bill is paid in full
	 */
	public boolean isPaid() {
		return paid = this.getAmountDue() == 0;
	}
	
	/**
	 * Get the amount still left to pay on this bill.
	 * @return the amount remaining
	 */
	public double getAmountDue() {
		return this.amount - this.amountPaid;
	}
	
	/**
	 * Get number of days until due
	 * @return days till the bill is due. If method returns -1, the bill for this period has been paid.
	 */
	public long daysTillDue() {
		if (paid) return -1L;
		
		Instant instant = new Instant();
		DateTime today = instant.toDateTime();
		
		if (today.getDayOfMonth() < this.dayDue) return this.dayDue - today.getDayOfMonth();
		
		DateTime next = new DateTime(today.plusMonths(1).getYear(), today.plusMonths(1).getMonthOfYear(), this.dayDue, 0, 0, 0, 0);
		Interval interval = new Interval(today, next);
		Duration duration = interval.toDuration();
		
		return duration.getStandardDays();
	}
	
	/**
	 * Pay the bill. Allows partial amounts as well and will automatically adjust saving intervals for partial amounts.
	 * @param toPay the amount being paid towards the bill. Can be partial.
	 */
	public void pay(double toPay) {
		
		if (toPay >= this.amount || (this.amountPaid + toPay) >= this.amount) {
			this.amountPaid = this.amount;
			this.paid = true;
		} else {
			this.amountPaid += toPay;
		}
		
	}
	
	/**
	 * Pay the amount in full.
	 */
	public void payInFull() {
		this.pay(this.amount);
	}
	
	/**
	 * For storage and saving purposes
	 * @return JSONObject with bill information and current standing
	 */
	public JSONObject toJSON() {
		JSONObject object = new JSONObject();
		
		object.put("name", this.name);
		object.put("dayDue", this.dayDue);
		object.put("amount", this.amount);
		object.put("amountPaid", this.amountPaid);
		
		return object;
	}
	
	/**
	 * Parse a bill from JSON
	 * @param object JSONObject storing the information of the bill
	 * @return Bill with current standing information assigned
	 */
	public static Bill parseBill(JSONObject object) {
		Bill bill = new Bill(object.getString("name"), object.getInt("dayDue"), object.getDouble("amount"));
		
		bill.amountPaid = object.getDouble("amountPaid");
		bill.isPaid();
		
		return bill;
	}
}
