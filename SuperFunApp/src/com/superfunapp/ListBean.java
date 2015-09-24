package com.superfunapp;

import java.io.Serializable;

public class ListBean implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private String amount;
	private String point;
	private String ticket;


	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	
	@Override
	public String toString() {
		return "ListBean [amount=" + amount + ", point=" + point + ", ticket="
				+ ticket + "]";
	}
}
