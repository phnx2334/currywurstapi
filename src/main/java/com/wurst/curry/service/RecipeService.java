package com.wurst.curry.service;

import java.util.List;

import com.wurst.curry.model.Recipe;
import com.wurst.curry.sqlObjects.RecipeIngredients;

public interface RecipeService {
	
//	Add recipe
	Recipe addRecipe(Recipe recipe);
	
//	Update a recipe
	Recipe updateRecipe(Recipe recipe);
	
//	Delete a recipe
	void deleteRecipe(long id);
	
//	Find recipe by ID
	Recipe findRecipeById(long id);
	
//	Find recipe by name
	Recipe findRecipeByName(String name);
	
//	Find recipe ingredients by name
	RecipeIngredients getRecipeIngredientsByName(String name);
	
//	Display all recipes
	List<Recipe> findAllRecipes();


}
