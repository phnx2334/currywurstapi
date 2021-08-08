package com.wurst.curry.service;

import java.util.List;

import com.wurst.curry.model.Orders;

public interface OrdersService {
	
	// Add an order
	Orders addOrder(Orders order);
	
	//Update order
	Orders updateOrder(Orders order);
	
	// cancel order (Delete)
	void deleteOrder(String orderId);
	
	// Display all orders
	List<Orders> findAllOrders();
	
	//Get order by ID
	Orders getOrderById(String orderId);
	
	//Find all orders and list from newest to oldest
	List<Orders> findAllByOrderByIdDesc();
	
	//Find all orders and list from oldest to newest
	List<Orders> findAllByOrderByIdAsc();
	
	//Get order items
	List<String> findAllOrderItems(String orderId);


}
