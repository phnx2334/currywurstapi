package com.wurst.curry.service;

import java.util.ArrayList;
import java.util.List;

import com.wurst.curry.model.IngredientsStorage;

public interface IngredientsStorageService {
	
	//Get ingredients given a recipe ID
	ArrayList<IngredientsStorage> findingredientNameByRecipeId(long id);
	
	//Get ingredient by name
	IngredientsStorage findByingredientName(String name);
	
	//Add new ingredient
	IngredientsStorage addIngredient(IngredientsStorage newIngredient);
	
	//Find all ingredients
	List<IngredientsStorage> findAllIngredients();
	
	//Delete recipe
	public void deleteStorageRecipe(Integer recipeId);
	
	//Change the quantity needed of an ingredient
	public void changeIngredientQuantity(Integer recipeId,  String ingredientName, Integer quantity);


}
