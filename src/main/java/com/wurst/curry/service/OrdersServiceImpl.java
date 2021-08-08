package com.wurst.curry.service;



import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wurst.curry.exception.ResourceNotFoundException;
import com.wurst.curry.model.CookingPlan;
import com.wurst.curry.model.Orders;
import com.wurst.curry.repository.OrdersRepository;
import com.wurst.curry.utils.NativeSql;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class OrdersServiceImpl implements OrdersService {

	@Autowired
	private OrdersRepository ordersRepository;
	@Autowired
	private CookingPlanService cookingPlanService;
	@Autowired
	NativeSql sql;
	@Autowired
	private EstimationService estimationService;
	
	
	
	//Add new order
	@Override
	public Orders addOrder(Orders order) {
		CookingPlan  builtOrder = new CookingPlan();
		LocalTime t1;
		LocalTime t2;
		String estimatedDeliveryTime;
		String expectedDeliveryTime = "";
	
		
		//Calculate estimated delivery time
		expectedDeliveryTime = order.getExpectedDeliveryTime();
		estimatedDeliveryTime = estimationService.estimateOrder(order.getOrderItems());
		
		//If estimated delivery time is <= expected delivery time, then I build the order, if not, I don't take it		
		//Calculate the difference between them
		//Expected delivery time
		t1 = LocalTime.parse(expectedDeliveryTime);
		//Estimated delivery time
		t2 = LocalTime.parse(estimatedDeliveryTime);
		
		if(t2.isBefore(t1)) {
			//Get built order
			builtOrder = cookingPlanService.buildOrder(order.getOrderItems());
			//Set delivery time according to estimation
			builtOrder.setDeliveryTime(estimatedDeliveryTime);			
			log.info("Order will be ready at " + builtOrder.getDeliveryTime());
			
			
			//Add order to the database
			//Set order id
			order.setId(builtOrder.getId());	
			//Set time received
			order.setTimeReceived(builtOrder.getTimeReceived());
			//Set delivery time
			order.setOrderDeliveryTime(builtOrder.getDeliveryTime());
			//Set price
			order.setPrice(builtOrder.getTotalPrice());
			//Set status of done
			order.setDone(false);
			
			//Add order to cooking plan
			cookingPlanService.addEntry(builtOrder);
			
			
			//Save order
			return this.ordersRepository.save(order);
			
		}else {
			throw new RuntimeException("We cannot prepare your order before the desired delivery time");
		}
	}
	
	
	//Update specific order TODO update scheduling of cooks and workers
	@Override
	public Orders updateOrder(Orders order) {
		Optional<Orders> orderDb = this.ordersRepository.findById(order.getId());
		
		if(orderDb.isPresent()) {
			Orders orderUpdate = orderDb.get();
			orderUpdate.setId(order.getId());
			orderUpdate.setOrderDeliveryTime(order.getOrderDeliveryTime());
			orderUpdate.setDone(order.isDone());
			orderUpdate.setTimeReceived(order.getTimeReceived());
			orderUpdate.setOrderItems(order.getOrderItems());
			orderUpdate.setPrice(order.getPrice());
			
			this.ordersRepository.save(orderUpdate);
			
			return orderUpdate;
		}else {//Throw exception if not found
			throw new ResourceNotFoundException("Menu Item not found with id:"+order.getId());
		}	
	}
	
	
	//Delete specific order by ID
	@Override
	public void deleteOrder(String orderId) {
		Optional<Orders> orderDb = this.ordersRepository.findById(orderId);
		
		if(orderDb.isPresent()) {
			this.ordersRepository.delete(orderDb.get());
		}else {
			throw new ResourceNotFoundException("There is no order with id "+orderId);
		}
		
	}

	//Find all orders
	@Override
	public List<Orders> findAllOrders() {
		return this.ordersRepository.findAll();
	}

	// Get specific order by ID
	@Override
	public Orders getOrderById(String orderId) {
		
		Optional<Orders> orderDb = this.ordersRepository.findById(orderId);
		
		if(orderDb.isPresent()) {
			return orderDb.get();
		}else {
			throw new ResourceNotFoundException("There is no Order with id "+orderId);
		}
	}
	
	//Find all orders and list from newest to oldest
	@Override
	public List<Orders> findAllByOrderByIdDesc() {
		return this.ordersRepository.findAllByOrderByIdDesc();
	}
	
	//Find all orders and list from oldest to newest
	@Override
	public List<Orders> findAllByOrderByIdAsc(){
		return this.ordersRepository.findAllByOrderByIdAsc();
	}
	
	// Find order items by ID
	@Override
	public List<String> findAllOrderItems(String orderId) {
		return this.sql.getOrderItemsById(orderId);
				
	}

}
