package com.wurst.curry.utils;

import java.util.UUID;

import org.springframework.stereotype.Component;


@Component
public class OrderIdGenerator {
	
	//Aide function that determines the order Id before it is registered in the database
	public String getOrderId() {
		return UUID.randomUUID().toString();
	}
}
