package com.wurst.curry.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.wurst.curry.model.Orders;
import com.wurst.curry.service.OrdersService;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/curryWurst")
public class OrdersController {
	
	@Autowired
	private OrdersService ordersService;
	
	// Make an order
	@PostMapping("/orders")
	public ResponseEntity<Orders> addOrder(@RequestBody Orders order) {
		return ResponseEntity.ok().body(this.ordersService.addOrder(order));
	}
	
	// Update order
	@PutMapping("orders/{id}")
	ResponseEntity<Orders> updateOrder(@PathVariable String id, @RequestBody Orders order){
		order.setId(id);
		return ResponseEntity.ok().body(this.ordersService.updateOrder(order));
	}
	
	
	// Delete order
	@DeleteMapping("orders/{id}")
	public HttpStatus deleteMenuItem(@PathVariable String id) {
		this.ordersService.deleteOrder(id);
		return HttpStatus.OK;
	}
	
	//List all orders
	@GetMapping("/orders")
	public ResponseEntity<List<Orders>> findAllOrders(){
		return ResponseEntity.ok(this.ordersService.findAllOrders());
	}
	
	// Get order by ID
	@GetMapping("/orders/{id}")
	public ResponseEntity<Orders> listOrders(@PathVariable String id){
		return ResponseEntity.ok(this.ordersService.getOrderById(id));
	}

}
