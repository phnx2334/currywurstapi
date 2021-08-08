package com.wurst.curry.model;

import java.sql.Array;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@JsonPropertyOrder({"id", "orderItems","timeReceived","expectedDeliveryTime","deliveryTime","totalPrice","itemsWorkers","beginCookingTime", "done"})
@Data
@Table(name="cooking_plan")
@Entity
public class CookingPlan {
	
	
	public CookingPlan() {
		super();
		this.id = "";
		this.orderItems = new HashMap<String, HashMap<String,Integer>>();
		this.cookDishes = new HashMap<String, HashMap<String,String>>();
		this.beginCookingTime = "";
		this.itemsWorkers = new HashMap<String, HashMap<String,String>>();
		this.deliveryTime = "";
		this.totalPrice = 0;
	}
	
	
	//References the order ID
	@Id
	private String id; 
	
	//References the items ordered and the quantity
	@Column(name="orderItems",columnDefinition = "LONGTEXT") 
	private HashMap<String, HashMap<String, Integer>> orderItems;
	
	//References the cook and the dish he prepares
	@Column(name="cook_dishes",columnDefinition = "LONGTEXT") 
	private HashMap<String, HashMap<String,String>> cookDishes;
	
	@Column(name="begin_cooking_time")
	@Setter (AccessLevel.NONE) private String beginCookingTime;
	
	//References the workers and carried items for the order
	@Column(name="items_workers",columnDefinition = "LONGTEXT") 
	private HashMap<String, HashMap<String, String>> itemsWorkers;
	
	@Column(name="delivery_time")
	@Setter (AccessLevel.NONE) private String deliveryTime;
	
	@Column(name="time_received")
	@Setter (AccessLevel.NONE) private String timeReceived;
	
	@Column(name="total_price")
	private int totalPrice;
	
	
	//CUSTOM SETTERS------------------------------------------------------

	//Declare a specific setter to set begin cooking time in format hh:mm
	public void setBeginCookingTime(int minutes) {
		String cookingStartTime = ""; 
		//Get current time
		LocalTime t1 = LocalTime.now();
		//Get time to add
		int hours = minutes / 60; 
		int mins = minutes % 60;
		//Convert time to add to localTime
		LocalTime t2 = LocalTime.of(hours, mins);
		LocalTime total = t1.plusHours(t2.getHour())
		                    .plusMinutes(t2.getMinute()); 
		cookingStartTime = total.format(DateTimeFormatter.ofPattern("HH:mm"));
		
		
		this.beginCookingTime = cookingStartTime;
	}

	
	//Declare a specific setter to set delivery time in format hh:mm
	public void setDeliveryTime(String estimatedDeliveryTime) {
			
		this.deliveryTime = estimatedDeliveryTime;
	}
	
	//Declare a specific setter to define order received time
	public void setTimeReceived () {
		//Get current time
			LocalTime t1 = LocalTime.now();
		
		this.timeReceived = t1.format(DateTimeFormatter.ofPattern("HH:mm"));
	}
	
	
	

}
