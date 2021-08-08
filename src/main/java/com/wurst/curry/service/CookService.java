package com.wurst.curry.service;

import java.util.List;

import com.wurst.curry.model.Cook;

public interface CookService {
	//Create a new cook
	Cook addCook(Cook cook);
	
	//Update cook
	Cook updateCook(Cook cook);
	
	//Delete cooks
	void deleteCook(long cookId);
	
	//List all cooks
	List<Cook> findAllCooks();
	
	//Get cook by name
	Cook findCookById(long id);
	
	//List all available Cooks
	List<Cook> findAllAvailableCooks();
	
	//Add to carried Items list
	void addRecipeToCook(Cook cook, String recipeName, String orderId );
	
	//Add busy time
	void addBusyTime(long cookId, long time);
	
	//Get cooks list from busiest to less busy
	List<Cook> findAllByOrderByTimeBusyDesc();
	
}
