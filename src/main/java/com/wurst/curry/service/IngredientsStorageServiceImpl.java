package com.wurst.curry.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wurst.curry.model.IngredientsStorage;
import com.wurst.curry.repository.IngredientsStorageRepository;





@Service
@Transactional
public class IngredientsStorageServiceImpl implements IngredientsStorageService {
	
	@Autowired
	IngredientsStorageRepository ingredientsStorageRepository;
	
	
	//Get ingredients given a recipe ID
	public ArrayList<IngredientsStorage> findingredientNameByRecipeId(long id){
		return this.ingredientsStorageRepository.findIngredientNameByRecipeId(id);
	}
	
	//Get ingredient by name
	public IngredientsStorage findByingredientName(String name) {
		return this.ingredientsStorageRepository.findByingredientName(name);
	}
	
	//Add new ingredient
	public IngredientsStorage addIngredient(IngredientsStorage newIngredient) {
		return this.ingredientsStorageRepository.save(newIngredient);
	}
	
	//Find all ingredients
	public List<IngredientsStorage> findAllIngredients(){
		return this.ingredientsStorageRepository.findAll();
	}
	
	//Delete recipe
	public void deleteStorageRecipe(Integer recipeId) {
		this.ingredientsStorageRepository.deleteStorageRecipe(recipeId);
	}
		
	//Change the quantity needed of an ingredient
	public void changeIngredientQuantity(Integer recipeId, String ingredientName,  Integer quantity) {
		this.ingredientsStorageRepository.modifyStorageIngredient(recipeId, ingredientName,quantity);
	}
	

}
