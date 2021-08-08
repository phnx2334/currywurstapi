package com.wurst.curry.service;

import java.util.List;

import com.wurst.curry.model.Storage;

public interface StorageService {
	
	//Add ingredient
	Storage addFoodItem(Storage foodItem);
	
	//Update ingredients
	Storage updateFoodItem(Storage foodItem);
	
	//Delete ingredient
	void deleteFoodItem(String name);
	
	//List all ingredients
	List<Storage> findAllFoodItems(); 
	
	//List all available ingredients
	List<Storage> findAllAvailableFoodItems();
	
	//Find ingredient by name
	Storage findByName(String name);

}
