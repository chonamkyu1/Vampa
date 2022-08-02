package com.vam.model;

import java.util.List;

public class OrderPageDTO {
	
	private List<OrderPageItemDTO> orders;
	
	public OrderPageDTO() {
		// TODO Auto-generated constructor stub
	}

	public List<OrderPageItemDTO> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderPageItemDTO> orders) {
		this.orders = orders;
	}

	@Override
	public String toString() {
		return "OrderPageDTO [orders=" + orders + "]";
	}
	
	
	
	
}
