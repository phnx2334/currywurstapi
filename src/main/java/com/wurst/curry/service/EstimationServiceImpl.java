package com.wurst.curry.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wurst.curry.model.Cook;
import com.wurst.curry.model.MenuItem;
import com.wurst.curry.model.Recipe;
import com.wurst.curry.model.Worker;
import com.wurst.curry.sqlObjects.MissingIngredients;

import com.wurst.curry.utils.NativeSql;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class EstimationServiceImpl implements EstimationService {
	
	
	@Autowired
	private MenuItemService menuItemService;
	@Autowired
	private WorkerService workerService;
	@Autowired
	private CookService cookService;
	@Autowired
	private RecipeService recipeService;
	

	
	@Autowired
	NativeSql sql;
	
	//Function made to estimate how much time the order would take to be ready.
	//Returns a string in format HH:MM referencing the time the order will be done
	public String estimateOrder(HashMap <String,Integer> items) {
		//Key = name
		//Value = quantity
		
		//I expect to receive a map with menu items and quantity of each
		
		//Variable to store total busy time to calculate order delivery time
		int totalBusyTime = 0;
		//Estimated delivery time
		String estimateDeliveryTime;
		
		//For each element I have to check if its available in the storage
		for(Entry<String, Integer> m:items.entrySet()) {

		
			//Check if element exists inside the menu table
			Optional<MenuItem> menuItemDb = Optional.of(this.menuItemService.findMenuItemByName(m.getKey()));
			
			if(menuItemDb.isPresent()) {
				//Validate if the recipe exists
				Recipe recipeDb = this.recipeService.findRecipeByName(m.getKey());
								
				if (recipeDb != null) {
			
					Recipe recipe = recipeDb;
					

					//Add busy time to the accumulator
					totalBusyTime = totalBusyTime <  estimateWorkers( m, (int)recipe.getId())? estimateWorkers( m, (int)recipe.getId()) :totalBusyTime;
				}
			}else {//Don't take the order because there is no such element in the menu
				log.info("We do not have " +m.getKey()+ " in the menu!");		
			}
		}
		

		//Estimate how much time will cooks take to cook the food
		totalBusyTime+= estimateCooks(items);
		
		//Convert the totalBusyTime to format HH:MM
		//Get current time
		LocalTime t1 = LocalTime.now();
		//Get time to add
		int hours = totalBusyTime / 60; 
		int minutes = totalBusyTime % 60;
		//Convert time to add to localTime
		LocalTime t2 = LocalTime.of(hours, minutes);
		LocalTime total = t1.plusHours(t2.getHour())
							.plusMinutes(t2.getMinute()); 
		estimateDeliveryTime = total.format(DateTimeFormatter.ofPattern("HH:mm"));
		
		log.info("The estimated delivery time is " + estimateDeliveryTime);

		return estimateDeliveryTime;

	}

	
	//Aide function to calculate how much time workers would take to look for all the ingredients
	//and bring them to the storage
	//Returns an int referencing the total sum of the minutes workers will take to get the ingredients
	public int estimateWorkers(Entry<String, Integer> m , int recipeId) {
		//Define variable to store the total amount of time to look for ingredients
		int busyTime = 0;
		//Define hashmap to store the workers and their busy times
		HashMap<String,Integer> workersBusyTime = new HashMap<String,Integer>();
		
		//Get all workers in the warehouse order by not busy first
		List<Worker> allWorkers =  this.workerService.findAllAvailableWorkers();
		
		//Determine whether it is a made product or not
		Recipe recipe = recipeService.findRecipeById(recipeId);
		
		List<MissingIngredients> missingIngredients;
		
		if(recipe.isPreMade()) {
			missingIngredients = sql.filterNeededIngredients(recipeId,m.getValue());
		}else {
			missingIngredients = sql.filterNeededIngredients(recipeId);
		}

		
		if(!(missingIngredients.isEmpty())) {
			//Find if there are available workers, if not I assign order to busy ones
			
			
			int currWorker =0;
			int nWorkers = allWorkers.size() - 1;
			//Assign each missing ingredient to a worker
			missf: for(MissingIngredients mItem : missingIngredients) {
				//For every unit needed, I assign it to a worker
				int g = 0;//Given units
				//While there are units needed
				while(g < mItem.getUnitsMissing()) {
					//get a worker by position in the list
					Worker worker = allWorkers.get(currWorker);
					
					int c = 0; //Capacity
					while(c < worker.getCapacity()) {//While worker is available to load
						//Add busy time for longest time ingredient
						if(c==0) {
							
							
							if(workersBusyTime.containsKey(worker.getName())) {
								int acummTime = workersBusyTime.get(worker.getName());
								acummTime+=mItem.getDeliveryTime();
								workersBusyTime.put(worker.getName(), acummTime);
							}else {
								//Check if the worker has previous busy time in the database
								int dbAccumTime= 0 ;
								if (!(worker.getTimeBusy() == 0)) {
									dbAccumTime = worker.getTimeBusy();
								}
								workersBusyTime.put(worker.getName(), mItem.getDeliveryTime() + dbAccumTime);
							}
						}

						c+=1;
						g+=1;
						
						//If all units missing have been assigned
						if (g == mItem.getUnitsMissing()) {
							//Restart the count if last worker is reached
							currWorker= currWorker == nWorkers? 0 : currWorker+1;
							
							continue missf;
						}
							
					}

					//Restart the count if last worker is reached
					currWorker= currWorker == nWorkers? 0 : currWorker+1;	
				
				}
			}
		}
		

		for(Entry<String,Integer> w:workersBusyTime.entrySet()) {
			busyTime = busyTime <  w.getValue()? w.getValue() :busyTime;
		}
		log.info("Total Busy time for workers is " + busyTime);
		return busyTime;
	}

	//Aide function to calculate how much time cooks would take to cook all the recipes
	//Returns an int referencing the total sum of the minutes cooks will take to get finish the recipes
	public int estimateCooks(HashMap <String,Integer> items) {
		//Estimate the time it will take to do the cooking
	
		//Define variable for storing the sum of busy time
		int busyTime=0;
		//Define hashmap to store the cooks and their busy times
		HashMap<String,Integer> cooksBusyTime = new HashMap<String,Integer>();
		
		//Get all cooks in the kitchen order by not busy first
		List<Cook> allCooks =  this.cookService.findAllAvailableCooks();
		
		
		int currCook =0;
		int nCooks = allCooks.size() - 1;
		
		
		//Get recipes to cook from the order
		//For each element in the list
		for(Entry<String, Integer> m:items.entrySet()) {
			
			//Check if element exists inside the menu table
			Optional<MenuItem> menuItemDb = Optional.of(this.menuItemService.findMenuItemByName(m.getKey()));
			
			if(menuItemDb.isPresent()) {
		
				Recipe recipeDb = this.recipeService.findRecipeByName(m.getKey());
				
				if (recipeDb != null) {
					//If its a recipe
					Recipe recipe = recipeDb;
					
					for (int i = 0; i < m.getValue(); i++) {
						//Find an available cook and assign it to him/her
						Cook cook = allCooks.get(currCook);
						//Add busy time
						
						if(cooksBusyTime.containsKey(cook.getName())) {
							int acummTime = cooksBusyTime.get(cook.getName());
							acummTime+=recipe.getTtc();
							cooksBusyTime.put(cook.getName(), acummTime);
						}else {
							int dbAccumTime= 0 ;
							if (!(cook.getTimeBusy() == 0)) {
								dbAccumTime = cook.getTimeBusy();
							}
							cooksBusyTime.put(cook.getName(), recipe.getTtc()+dbAccumTime);
						}

						//Assign the next cook to take the next recipe
						currCook= currCook == nCooks? 0 : currCook+1;
					
					}			
				}			
			}
		}
		
		
		for(Entry<String,Integer> c:cooksBusyTime.entrySet()) {
			busyTime = busyTime <  c.getValue()? c.getValue() :busyTime;
		}
		log.info("Total Busy time for cooks is " + busyTime);
		return busyTime;
	}
}
