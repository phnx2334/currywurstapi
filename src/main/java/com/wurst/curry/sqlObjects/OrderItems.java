package com.wurst.curry.sqlObjects;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;


//Class used to map the order items query, declared in file nativeSQL
@Data
@Component
public class OrderItems {
	
	long id;
	List<String> items;
	
	
	public OrderItems(){}
	

	public  OrderItems(List<Object[]> query) {
		
		for(Object row : query)
			this.items.add((String) row);
    }

}
