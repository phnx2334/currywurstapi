package com.wurst.curry.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wurst.curry.exception.ResourceNotFoundException;
import com.wurst.curry.exception.CookingPlanException;
import com.wurst.curry.model.Cook;
import com.wurst.curry.model.CookingPlan;
import com.wurst.curry.model.MenuItem;
import com.wurst.curry.model.Recipe;
import com.wurst.curry.model.Worker;
import com.wurst.curry.repository.CookingPlanRepository;
import com.wurst.curry.utils.EnableDisable;
import com.wurst.curry.utils.NativeSql;
import com.wurst.curry.utils.OrderIdGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class CookingPlanServiceImpl implements CookingPlanService{
	
	@Autowired
	CookingPlanRepository cookingPlanRepository;  
	
	@Autowired
	private MenuItemService menuItemService;
	@Autowired
	private RecipeService recipeService;
	@Autowired
	private CookService cookService;
	@Autowired
	private ScheduleService scheduleService;
	
	
	@Autowired
	EnableDisable enableDisable;
	@Autowired
	NativeSql sql;
	@Autowired
	OrderIdGenerator idGenerator;
	
	

	//Add entry to the cooking plan
	@Override
	public CookingPlan addEntry(CookingPlan entry) {
		return this.cookingPlanRepository.save(entry);
	}
	//Update entry in the cooking plan
	@Override
	public CookingPlan updateEntry(CookingPlan entry) {
		Optional<CookingPlan> cookingPlanDb = this.cookingPlanRepository.findById(entry.getId());
		
		if(cookingPlanDb.isPresent()) {
			CookingPlan cookingPlanUpdate = cookingPlanDb.get();
			cookingPlanUpdate.setId(entry.getId());
			cookingPlanUpdate.setOrderItems(entry.getOrderItems());
			//cookingPlanUpdate.setBeginCookingTime(entry.getBeginCookingTime());
			cookingPlanUpdate.setCookDishes(entry.getCookDishes());
			//cookingPlanUpdate.setDeliveryTime(entry.getDeliveryTime());
			cookingPlanUpdate.setItemsWorkers(entry.getItemsWorkers());
			//cookingPlanUpdate.setTimeReceived(entry.getTimeReceived());
			cookingPlanUpdate.setTotalPrice(entry.getTotalPrice());
		}
		return null;
	}
	//Delete entry of the cooking plan
	@Override
	public void deleteEntry(String entryId) {
		Optional<CookingPlan> cookingPlanDb =  this.cookingPlanRepository.findById(entryId);
		
		if(cookingPlanDb.isPresent()) {
			this.cookingPlanRepository.delete(cookingPlanDb.get());
		}else {
			throw new CookingPlanException("There is no cooking plan with id "+entryId);
		}
	}
	
	//Get the cooking plan for all orders
	@Override
	public List<CookingPlan> getCookingPlan() {
		return this.cookingPlanRepository.findAll();
	}
	
	
	
	//Get cooking plan for specific order
	@Override
	public CookingPlan getCookingPlanById(String entryId) {
		Optional<CookingPlan> cookingPlanDb = this.cookingPlanRepository.findById(entryId);
		
		if(cookingPlanDb.isPresent()) {
			return cookingPlanDb.get();
		
		}else {
			throw new ResourceNotFoundException("Cooking plan not found with id:"+entryId);
		}
	}

	
		//This function builds and returns an object referencing the class Cooking plan
		//It also executes aide functions which assign dishes to cooks and ingredients to workers
		public CookingPlan buildOrder(HashMap <String,Integer> items) {		
			//Variable to store time to add to the order for missing items and unavailable cooks
			int timeToAdd = 0;
			//Variable to store total price
			int totalPrice = 0;
			int itemPrice = 0;
			//Variable to store the hash map to return
			CookingPlan cookingPlan = new CookingPlan();
			//Variable to store the return object from scheduling functions
			HashMap<String, String> scheduled = new HashMap <String,String>();
			//Determine the order UUID
			String orderId = idGenerator.getOrderId();
			
			//For each element I have to check if its available in the storage
			for(Entry<String,Integer> m:items.entrySet()) {
			
				//Check if element exists inside the menu table
				Optional<MenuItem> menuItemDb = Optional.of(this.menuItemService.findMenuItemByName( m.getKey()));
				
				if(menuItemDb.isPresent()) {
					//log.info("The element " +item+ " exists in the menu.");
					//Validate if its a recipe or a made product
					Recipe recipeDb = this.recipeService.findRecipeByName( m.getKey());
					
					//Add price to the total price of the order
					MenuItem menuItem = menuItemDb.get();
					itemPrice =  menuItem.getPrice();
					totalPrice+=   menuItem.getPrice() * m.getValue() ;
					
					if (recipeDb != null) {
						//If its a recipe
						//log.info("The element " +item+ " is a recipe");
						Recipe recipe = recipeDb;
						
						
						//Call function to schedule workers
						scheduled =	 scheduleService.scheduleWorkers(m, (int)recipe.getId(), orderId);
						//Schedule workers as many time as the recipe is asked
						for(Entry <String, String> wp:scheduled.entrySet()) {
							HashMap<String, String> orderAndItems = new HashMap<String, String>();
							orderAndItems.put(orderId, wp.getValue());
							
							if(cookingPlan.getItemsWorkers().containsKey(wp.getKey())) {
								HashMap<String, String> tempHash = cookingPlan.getItemsWorkers().get(wp.getKey());
								tempHash.put(orderId, wp.getValue());
								cookingPlan.getItemsWorkers()
									.put(wp.getKey(), tempHash );
							}else {
								cookingPlan.getItemsWorkers()
								.put(wp.getKey(), orderAndItems );
								//.put(cp.getKey(), cp.getValue());
							}

						}
					}
				}else {//Don't take the order because the element doesn't exists in the menu
					log.info("We do not have " +m.getKey()+ " in the menu!");	
					throw new ResourceNotFoundException("We do not have " +m.getKey()+ " in the menu!");
					
				
				}
				//Store quantity ordered and price of the item
				HashMap<String,Integer> quantityAndPrice = new HashMap<String,Integer>();
				quantityAndPrice.put("quantity", m.getValue());
				quantityAndPrice.put("price", itemPrice* m.getValue());
				//Save order items in returning object
				cookingPlan.getOrderItems().put((String)m.getKey(), quantityAndPrice );

			}
			
			//Call function to set NTA of all workers
			List<Worker> busyWorkers = scheduleService.setWorkersNTA();
			
			
			//Get the time to add to the order because of missing ingredients
			timeToAdd+=busyWorkers.get(0).getTimeBusy();
			log.info("The extra time to add to the order to look for ingredients is " + String.valueOf(timeToAdd) );
			
			//Define start cooking time in cooking plan based on time taken to gather all ingredients
			cookingPlan.setBeginCookingTime(timeToAdd);
			
			//Get when will the next worker be available
			//String nAvlWorker = busyWorkers.get(busyWorkers.size()-1).getNTA();
			//log.info("Next available worker will be at " + nAvlWorker);
			
			//Schedule cooks to cook the food
			scheduled = scheduleService.scheduleCooks(items, orderId, busyWorkers.get(0).getTimeBusy());
			
			//Map the scheduled dishes and cooks to the cooking plan object
			for(Entry <String, String> cp:scheduled.entrySet()) {
				HashMap<String, String> orderAndItem = new HashMap<String, String>();
				orderAndItem.put(orderId, cp.getValue());
				
				if(cookingPlan.getCookDishes().containsKey(cp.getKey())) {
					HashMap<String, String> tempHash = cookingPlan.getCookDishes().get(cp.getKey());
					tempHash.put(orderId, cp.getValue());
					cookingPlan.getCookDishes()
						.put(cp.getKey(), tempHash );
				}else {
					cookingPlan.getCookDishes()
					.put(cp.getKey(), orderAndItem );
					//.put(cp.getKey(), cp.getValue());
				}

			}
			
			//Find busiest cook
			List<Cook> allBusyCooks =  this.cookService.findAllByOrderByTimeBusyDesc();
			
			//Get the time to add to the order because of cooking time
			timeToAdd+=allBusyCooks.get(0).getTimeBusy();
			log.info("The extra time to add to the order for cooking is " + String.valueOf(allBusyCooks.get(0).getTimeBusy()) );
			
			
			//Get when will the next cook be available
			//String nAvlCook = allBusyCooks.get(allBusyCooks.size()-1).getNTA();
			//log.info("Next available cook will be at " + nAvlCook);
			
			
			log.info("Total time to add to the order is " + String.valueOf(timeToAdd));
			
			
			//Set values of time for delivery,total price and order ID
			cookingPlan.setTotalPrice(totalPrice);
			cookingPlan.setId(orderId);
			cookingPlan.setTimeReceived();
			
			return cookingPlan;

		}
}
