package com.wurst.curry.utils;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wurst.curry.model.Cook;
import com.wurst.curry.model.Orders;
import com.wurst.curry.model.Worker;
import com.wurst.curry.service.CookService;
import com.wurst.curry.service.OrdersService;
import com.wurst.curry.service.RecipeService;
import com.wurst.curry.service.WarehouseService;
import com.wurst.curry.service.WorkerService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Component
@Configuration
@EnableScheduling
@EnableAsync
public class EnableDisable {
	
	@Autowired
	private WorkerService workerService;
	@Autowired
	private CookService cookService;
	@Autowired
	private OrdersService ordersService;
	@Autowired
	private RecipeService recipeService;
	@Autowired
	private WarehouseService warehouseService;
	
	//Enables (available = true) workers and cooks at NTA for each person
	@Async
	@Scheduled(fixedDelay = 1000)
	public void checkWorkersAvailability() {
		//Get all workers from the database
		List<Worker> workersDb = this.workerService.findAllWorkers();
		
		//Go through the list and activate any worker who is still unavailable and is past its NTA
		for(Worker w :workersDb) {
			if(w.getTimeBusy()>0) {
				//Get current time
				LocalTime t1 = LocalTime.now();
				//Get time to be available
				LocalTime t2 = LocalTime.parse(w.getNTA());
				
				if(t1.isAfter(t2)) {
					w.setAvailable(true);
					w.setTimeBusy(0);
					this.workerService.updateWorker(w);
					//log.info("Worker " + w.getName() + " is now available+++");
				}
			}
			
		}
	}
	
	@Async
	@Scheduled(fixedDelay = 1000)
	public void checkCooksAvailability() {
		//Get all cooks from the database
		List<Cook> cooksDb = this.cookService.findAllCooks();
		
		//Go through the list and activate any cook who is still unavailable and is past its NTA
		for(Cook c :cooksDb) {
			if(c.getTimeBusy()>0) {
				//Get current time
				LocalTime t1 = LocalTime.now();
				//Get time to be available
				LocalTime t2 = LocalTime.parse(c.getNTA());
				
				
				if(t1.isAfter(t2)) {
					c.setAvailable(true);
					c.setTimeBusy(0);
					this.cookService.updateCook(c);
					//log.info("Cook " + c.getName() + " is now available+++");
				}
			}
			
		}
	}
	
	
	//Sets orders to done = true once their delivery time has been reached
	@Async
	@Scheduled(fixedDelay = 1000)
	public void setOrdersToDone() {
		//Get all orders from the database
		List<Orders> ordersDb = this.ordersService.findAllByOrderByIdAsc();
		
		if (!(ordersDb.isEmpty())) {
			//Go through the list and set done = true any order that is past its deliveryTime
			for(Orders o :ordersDb) {
				if(!(o.isDone())) {
					//Get current time
					LocalTime t1 = LocalTime.now();
					//Get time to be available
					LocalTime t2 = LocalTime.parse(o.getOrderDeliveryTime());
					
					
					if(t1.isAfter(t2)) {
						o.setDone(true);
						this.ordersService.updateOrder(o);
						log.info("Order " + o.getId() + " is now finished+++");
						
						//Modify cooks assigned orders
						List<Cook> allCooks = cookService.findAllCooks();
						for(Cook c:allCooks) {
							//Define time spent on cooking given by sum of order items
							int timeCooking = 0;
							String[] cookedItems;
							if(!(c.getRecipesToCook() == null)) {
								//Get recipes to cook
								HashMap<String,String> recipesToCook = c.getRecipesToCook();
								if(recipesToCook.containsKey(o.getId())) {
									
									//Get items cooked for current order
									cookedItems = recipesToCook.get(o.getId()).split(",");
									//Calculate time taken to cook
									for(String item:cookedItems) {
										timeCooking+= recipeService.findRecipeByName(item).getTtc();
									}
									//Delete order from the list of orders to cook
									recipesToCook.remove(o.getId());
									Cook updateCook = c;
									updateCook.setRecipesToCook(recipesToCook);
									//Subtract time to busy time
									int cookBusyTime = c.getTimeBusy();
									if(cookBusyTime-timeCooking <=0) {
										updateCook.setTimeBusy(0);
									}else {
										updateCook.setTimeBusy(cookBusyTime-timeCooking);
									}
									
									this.cookService.updateCook(updateCook);
	
								}
								
							}	
						}
						
						//Modify workers assigned orders
						List<Worker> allWorkers = workerService.findAllWorkers();
						for(Worker w:allWorkers) {
							//Define time spent on bringing all items
							int timeCarrying = 0;
							String[] broughtItems;
							if(w.getItems() != null) {
								
								//Get items to carry
								HashMap<String,String> itemsToCarry = w.getItems();
								if(itemsToCarry.containsKey(o.getId())) {
									broughtItems = itemsToCarry.get(o.getId()).split(",");
									//Calculate time to bring all of them
									for(String item:broughtItems) {
										timeCarrying+= warehouseService.findIngredientByName(item).getDeliveryTime();
									}
									//Delete order from orders to carry for each worker
									
									itemsToCarry.remove(o.getId());
									
									Worker updateWorker = w;
									updateWorker.setItems(itemsToCarry);
									//Subtract time to busy time
									int workerBusyTime = w.getTimeBusy();
									if(workerBusyTime-timeCarrying <=0) {
										updateWorker.setTimeBusy(0);
									}else {
										updateWorker.setTimeBusy(workerBusyTime-timeCarrying);
									}
									
									this.workerService.updateWorker(updateWorker);
									
								}
							}
						}
					}
				}
			}	
		}
	}
}
