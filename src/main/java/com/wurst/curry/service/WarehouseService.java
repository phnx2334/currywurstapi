package com.wurst.curry.service;

import java.util.List;

import com.wurst.curry.model.Warehouse;

public interface WarehouseService {
	
	//Add ingredient
	Warehouse addIngredient(Warehouse ingredient);
	
	//Update ingredients
	Warehouse updateIngredient(Warehouse ingredient);
	
	//Delete ingredient
	void deleteIngredient(long ingredientId);
	
	//List all ingredients
	List<Warehouse> findAllIngredients(); 
	
	//Find ingredient by name
	Warehouse findIngredientByName(String name);
	
}
