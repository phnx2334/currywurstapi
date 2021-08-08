package com.wurst.curry.model;

import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


import jdk.jfr.Timestamp;
import lombok.Data;

@Data
@Entity
@Table(name="orders")
public class Orders {
	
	@Id
	private String id;
	
	@Column(name="time_received")
	private String timeReceived;
	
	@Timestamp
	@Column(name="order_delivery_time")
	private String orderDeliveryTime ;
	
	@Column(name="price")
	private int price;

	@Column(name="items",columnDefinition = "LONGTEXT")
	private HashMap<String, Integer> orderItems;
	
	//For controlling when an order has been completed
	@Column(name="done")
	private boolean done;

	//For defining maximum time to handle the order to the client 
	@Column(name="expected_delivery_time")
	private String expectedDeliveryTime;
	
	
	
	
	
	
	

}
