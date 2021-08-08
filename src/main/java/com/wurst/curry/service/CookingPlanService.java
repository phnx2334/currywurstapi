package com.wurst.curry.service;

import java.util.HashMap;
import java.util.List;

import com.wurst.curry.model.CookingPlan;

public interface CookingPlanService {
	
	//Add an entry to the cooking plan
	CookingPlan addEntry(CookingPlan entry);
	
	//Update an entry in the cooking plan
	CookingPlan updateEntry(CookingPlan entry);
	
	//Delete an entry in the cooking plan
	void deleteEntry(String entryId);
	
	//Show cooking plan
	List<CookingPlan> getCookingPlan();
	
	//Show plan for specific ID
	CookingPlan getCookingPlanById(String entryId);
	
	//Build cooking plan scheduling workers and cooks
	CookingPlan buildOrder(HashMap <String,Integer> items);
	

}
