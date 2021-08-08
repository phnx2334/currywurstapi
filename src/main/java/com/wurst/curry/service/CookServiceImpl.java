package com.wurst.curry.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.wurst.curry.exception.ResourceNotFoundException;
import com.wurst.curry.model.Cook;
import com.wurst.curry.repository.CookRepository;

@Service
@Transactional
public class CookServiceImpl implements CookService{
	
	//Instantiate repository object to make the transactions
	@Autowired
	private CookRepository cookRepository;
	
	//Create new cook
	@Override
	public Cook addCook(Cook cook) {
		try {
			return cookRepository.save(cook);
		}catch(RuntimeException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
		}
		
	}
	
	//Update cook
	@Override
	public Cook updateCook(Cook cook) {
		Optional<Cook> cookDb = this.cookRepository.findById(cook.getId());
		
		if(cookDb.isPresent()) {
			Cook cookUpdate = cookDb.get();
			
			cookUpdate.setId(cook.getId());
			cookUpdate.setName(cook.getName());
			cookUpdate.setAvailable(cook.isAvailable());
			cookUpdate.setNTA(cook.getNTA());
			cookUpdate.setRecipesToCook(cook.getRecipesToCook());
			cookUpdate.setTimeBusy(cook.getTimeBusy());
			this.cookRepository.save(cookUpdate);
			
			return cookUpdate;
		}else {//Throw exception if not found
			throw new ResourceNotFoundException("Cook not found with id:"+cook.getId());
		}
	}
	
	//Delete cook by id
	@Override
	public void deleteCook(long cookId) {
		Optional<Cook> cookDb = this.cookRepository.findById(cookId);
		
		if(cookDb.isPresent()) {
			this.cookRepository.delete(cookDb.get());
		}else {
			throw new ResourceNotFoundException("There is no cook with id "+cookId);
		}
		
	}
	
	//List all created cooks
	@Override
	public List<Cook> findAllCooks() {
		return this.cookRepository.findAll();
	}
	
	
	//List all cooks that are available
	@Override
	public List<Cook> findAllAvailableCooks() {
		return this.cookRepository.findAllByOrderByAvailableDesc();
	}
	
	//Find cook by ID
	@Override
	public Cook findCookById(long id) {
		//Get cook ID from parameter
		Optional<Cook> cookDb = this.cookRepository.findById(id);
		//If cook ID is found
		if(cookDb.isPresent()) {
			return cookDb.get();
		}else {
			throw new ResourceNotFoundException("Cook not found with id:"+id);
		}
	}

	//Add order ID and recipes to cooking list of cook
	@Override
	public void addRecipeToCook(Cook cook, String recipeName, String orderId) {
		Optional<Cook> cookDb = this.cookRepository.findById(cook.getId());
		
		if(cookDb.isPresent()) {
			Cook updateCook = cookDb.get();
			//Get already listed recipes
			HashMap<String, String> currList = updateCook.getRecipesToCook();
			
			String recipes = "";
			if(!(currList == null)) {
				//Evaluate if key for order already exists
				if(currList.containsKey(orderId)) {
					recipes = currList.get(orderId);
					//Add new recipe
					recipes = recipes + ","+recipeName;
					//Delete the first comma
					if(recipes.substring(0, 1) == ",") {
						recipes = recipes.substring(1);
					}
					currList.put(orderId, recipes);
				}else {//If not, add a new order with the recipe name
					currList.put(orderId, recipeName);
				}
			}else {//If is empty, create a new list with order id and recipe name
				currList = new HashMap<String,String>();
				currList.put(orderId, recipeName);
			}
			
			//Update the database with the new recipes
			updateCook.setRecipesToCook(currList);
			updateCook(updateCook);
			
		}else {
			throw new ResourceNotFoundException("There is no cook with id "+cook.getId());
		}
		
	}

	//Add busy time
	@Override
	public void addBusyTime(long cookId, long time) {
		Optional<Cook> cookDb = this.cookRepository.findById(cookId);
		
		if(cookDb.isPresent()) {
			Cook updateCook = cookDb.get();
			int currTime = updateCook.getTimeBusy();
			currTime+=time;
			updateCook.setTimeBusy(currTime);
			updateCook.setAvailable(false);
			updateCook(updateCook);
			
		}else {
			throw new ResourceNotFoundException("There is no cook with id "+cookId);
		}
		
	}

	//Get cooks list from busiest to less busy
	@Override
	public List<Cook> findAllByOrderByTimeBusyDesc() {
		return this.cookRepository.findAllByOrderByTimeBusyDesc();
	}
	
	
}
