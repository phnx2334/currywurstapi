package com.wurst.curry.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wurst.curry.model.Cook;
import com.wurst.curry.model.MenuItem;
import com.wurst.curry.model.Recipe;
import com.wurst.curry.model.Worker;
import com.wurst.curry.sqlObjects.MissingIngredients;
import com.wurst.curry.sqlObjects.RecipeIngredients;
import com.wurst.curry.utils.EnableDisable;
import com.wurst.curry.utils.NativeSql;
import com.wurst.curry.utils.OrderIdGenerator;
import com.wurst.curry.utils.UpdateStorageIngredients;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {
	
	@Autowired
	private MenuItemService menuItemService;
	@Autowired
	private WorkerService workerService;
	@Autowired
	private RecipeService recipeService;
	@Autowired
	private CookService cookService;


	
	@Autowired
	EnableDisable enableDisable;
	@Autowired
	NativeSql sql;
	@Autowired
	OrderIdGenerator idGenerator;
	
	
	@Autowired
	private ApplicationContext applicationContext;
	
		
	//Aide function to schedule workers and assign ingredients for them to carry based on their
	//capacity
	//Returns a hashmap with workers and their items to carry for an specific order
	public HashMap<String, String> scheduleWorkers(Entry<String, Integer> m , int recipeId,String orderId) {
		//Define the HashMap to return once the scheduling is finished
		HashMap<String,String> wplan = new HashMap<String,String>();
		
		//Determine whether it is a made product or not
		Recipe recipe = recipeService.findRecipeById(recipeId);
		
		List<MissingIngredients> missingIngredients;
		
		if(recipe.isPreMade()) {
			missingIngredients = sql.filterNeededIngredients(recipeId,m.getValue());
		}else {
			missingIngredients = sql.filterNeededIngredients(recipeId);
		}
		
		
		if(!(missingIngredients.isEmpty())) {
//			for(MissingIngredients mItem : missingIngredients) {
//				log.info(("Missing " + String.valueOf(mItem.getUnitsMissing()) +" units of " + mItem.getName() +" in the storage to prepare it."));
//			}
			
	
			//Find if there are available cooks in the storage, if not I assign order to busy ones
			List<Worker> allWorkers;
			List<Worker> allAvailableWorkers =  this.workerService.findAllAvailableWorkers();
			if(allAvailableWorkers.isEmpty()) {
				allWorkers =  this.workerService.findAllWorkers();
			}else {
				allWorkers = allAvailableWorkers;
			}
			
			int currWorker =0;
			int nWorkers = allWorkers.size() - 1;
			//Assign each missing ingredient to a worker
			missf: for(MissingIngredients mItem : missingIngredients) {
				//For every unit needed, I assign it to a worker
				int g = 0;//Given units
				//While there are units needed
				while(g < mItem.getUnitsMissing()) {
//					//Reserve (decrease) ingredients that are already in the storage for current recipe
//					UpdateStorageIngredients di = new UpdateStorageIngredients();
//					applicationContext.getAutowireCapableBeanFactory().autowireBean(di);
//					di.decreaseIngredient(mItem.getName(), mItem.getStorageUnits());
					
					//get a worker by position in the list
					Worker worker = allWorkers.get(currWorker);
					
					int c = 0; //Capacity
					while(c < worker.getCapacity()) {//While worker is available to load
						//Add item to list
						this.workerService.addCarriedItem(worker.getId(), mItem.getName(), orderId );
						//Add busy time for longest time ingredient
						if(c==0) {
							this.workerService.addBusyTime(worker.getId(), mItem.getDeliveryTime());
//							log.info("Added "+  String.valueOf(mItem.getDeliveryTime())
//							+ " minutes for worker "+ worker.getName());
						}
//						log.info(" for ingredient "+ mItem.getName() + " at capacity " + String.valueOf(c));
						
						
						
						c+=1;
						g+=1;
						
						//If all units missing have been assigned
						if (g == mItem.getUnitsMissing()) {
							//Restart the count if last worker is reached
							currWorker= currWorker == nWorkers? 0 : currWorker+1;
							
							//Save data for the cooking plan
							wplan.put(worker.getName(),worker.getItems().get(orderId));
							
							//Set timer to update ingredient in the storage once the delivery time passes
							Timer timer = new Timer();
							//Add delivery minutes to current time
							Calendar now = Calendar.getInstance();
							now.add(Calendar.MINUTE, mItem.getDeliveryTime());
							
							//Schedule to execute task in given time
							UpdateStorageIngredients ki = new UpdateStorageIngredients("increase", mItem.getName(), g);
							applicationContext.getAutowireCapableBeanFactory().autowireBean(ki);
							timer.schedule(ki, now.getTime());
//							log.info("Adding missing ingredient "+ mItem.getName()+" to storage in "+ String.valueOf(mItem.getDeliveryTime()) + " minutes");
							try {
								Thread.sleep((long) Math.random()*2000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							continue missf;
						}
							
					}
					//Save data for the cooking plan
					
					wplan.put(worker.getName(), worker.getItems().get(orderId));
						
						
					//Set worker as unavailable
					worker.setAvailable(false);
					this.workerService.updateWorker(worker);
					//Restart the count if last worker is reached
					currWorker= currWorker == nWorkers? 0 : currWorker+1;	
				}
			}
		}
		
		return wplan;
	}


	
	//Aide function to schedule cooks and assing recipes to be cook
	//Returns a hashmap with cooks and their recipes to cook for an specific order
	public HashMap<String, String> scheduleCooks(HashMap<String, Integer> items, String orderId, Integer workersTime) {		
		//Define the HashMap to return once the scheduling is finished
		HashMap<String, String> cPlan = new HashMap<String, String>();
		
		//Get all cooks in the storage order by not busy first
		List<Cook> allCooks =  this.cookService.findAllAvailableCooks();
		
		
		int currCook =0;
		int nCooks = allCooks.size() - 1;
		
		//Get recipes to cook from the order
		//For each element in the list
		for(Entry<String, Integer> m:items.entrySet()) {
			
			//Check if element exists inside the menu table
			Optional<MenuItem> menuItemDb = Optional.of(this.menuItemService.findMenuItemByName(m.getKey()));
			
			if(menuItemDb.isPresent()) {
				//log.info("The element " +item+ " exists in the menu.");
				//Validate if its a recipe or a made product
				Recipe recipeDb = this.recipeService.findRecipeByName(m.getKey());
				
				if (recipeDb != null) {
					//If its a recipe
					//log.info("The element " +item+ " is a recipe");
					Recipe recipe = recipeDb;
					
					for (int i = 0; i < m.getValue(); i++) {
						//Find an available cook and assign it to him/her, take out the items from the storage
						Cook cook = allCooks.get(currCook);
						//Add recipe to cooking list
						this.cookService.addRecipeToCook(cook, recipe.getName(), orderId);
						//Add busy time
						this.cookService.addBusyTime(cook.getId(), recipe.getTtc() + workersTime);
						
						
//						log.info("Added "+  String.valueOf(recipe.getTtc())
//						+ " minutes for cook "+ cook.getName() + " for recipe "
//						+ recipe.getName());
						
						cPlan.put(cook.getName(),cook.getRecipesToCook().get(orderId));
						
						//Decrease items in the kitchen for that particular recipe
						RecipeIngredients recipeIngredients;
						
						UpdateStorageIngredients di = new UpdateStorageIngredients();
						applicationContext.getAutowireCapableBeanFactory().autowireBean(di);
						
						recipeIngredients = sql.getRecipeIngredients(recipe.getName());
						
						for(HashMap<String, Integer> mItem : recipeIngredients.getIngredientsList()) {
							String key = (String) mItem.keySet().toArray()[0];
							di.decreaseIngredient(key, mItem.get(key).intValue());
						}

						//Assign the next cook to take the next recipe
						currCook= currCook == nCooks? 0 : currCook+1;
					}			
				}			
			}
		}
		//Set NTA for all cooks
		setCooksNTA();
		
		return cPlan;
	}


	
	//Aide function that settles the next time available field in the worker table based on how much
	//busy time do they have.
	//Returns a list of workers ordered desc considering the time busy
	public List<Worker> setWorkersNTA() {

		//Find busiest worker and define NTA
		List<Worker> allBusyWorkers = this.workerService.findAllByOrderByTimeBusyDesc();
		for(Worker worker : allBusyWorkers) {
//			if(worker.getTimeBusy() > 0) {
//				log.info("Worker " + worker.getName() + " is busy for the next " 
//						+ String.valueOf(worker.getTimeBusy()) + " minutes" );
//			}
			
			//Update NTA
			//Get current time
			LocalTime t1 = LocalTime.now();
			//Get time to add
			int hours = worker.getTimeBusy() / 60; 
			int minutes = worker.getTimeBusy() % 60;
			//Define NTA
			LocalTime t2 = LocalTime.of(hours, minutes);
			LocalTime total = t1.plusHours(t2.getHour())
								.plusMinutes(t2.getMinute()); 
			String time = total.format(DateTimeFormatter.ofPattern("HH:mm"));
			
			//Update NTA
			worker.setNTA(time);
			this.workerService.updateWorker(worker);
			
			
		}
		return allBusyWorkers;
	}

	//Aide function that settles the next time available field in the cook table based on how much
	//busy time do they have.
	//Returns a list of cooks ordered desc considering the time busy
	public List<Cook> setCooksNTA (){
		List<Cook> allBusyCooks = this.cookService.findAllByOrderByTimeBusyDesc();
		for(Cook cook : allBusyCooks) {
//			if (cook.getTimeBusy()>0) {
//				log.info("Cook " + cook.getName() + " is busy for the next " 
//						+ String.valueOf(cook.getTimeBusy()) + " minutes" );
//			}
			
			//Update NTA
			//Get current time
			LocalTime t1 = LocalTime.now();
			//Get time to add
			int hours = cook.getTimeBusy() / 60; 
			int minutes = cook.getTimeBusy() % 60;
			//Define NTA
			LocalTime t2 = LocalTime.of(hours, minutes);
			LocalTime total = t1.plusHours(t2.getHour())
								.plusMinutes(t2.getMinute()); 
			String time = total.format(DateTimeFormatter.ofPattern("HH:mm"));
			
			//Update NTA
			cook.setNTA(time);
			this.cookService.updateCook(cook);
			
		}
		return allBusyCooks;
	}
}
