package com.wurst.curry.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wurst.curry.exception.ResourceNotFoundException;
import com.wurst.curry.model.Recipe;
import com.wurst.curry.repository.RecipeRepository;
import com.wurst.curry.sqlObjects.RecipeIngredients;
import com.wurst.curry.utils.NativeSql;



@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {
	
	@Autowired
	private RecipeRepository recipeRepository;
	@Autowired
	NativeSql sql;

	//Add new recipe
	@Override
	public Recipe addRecipe(Recipe recipe) {
		return this.recipeRepository.save(recipe);
	}

	//Update Recipe
	@Override
	public Recipe updateRecipe(Recipe recipe) {
		Optional<Recipe> recipeDb = this.recipeRepository.findById(recipe.getId());
		
		if (recipeDb.isPresent()) {
			Recipe recipeUpdate = recipeDb.get();
			recipeUpdate.setId(recipe.getId());
			recipeUpdate.setName(recipe.getName());
			recipeUpdate.setNutrients(recipe.getNutrients());
			recipeUpdate.setTtc(recipe.getTtc());
			this.recipeRepository.save(recipeUpdate);
			
			return recipeUpdate;
		}else {//Throw exception if not found
			throw new ResourceNotFoundException("Recipe not found with id:"+recipe.getId());
		}
		
	}

	//Delete Recipe
	@Override
	public void deleteRecipe(long id) {
		Optional<Recipe> recipeDb = this.recipeRepository.findById(id);
		
		if(recipeDb.isPresent()) {
			this.recipeRepository.delete(recipeDb.get());
		}else {
			throw new ResourceNotFoundException("Recipe not found with id "+id);
		}
		
	}
	
	//Get recipe by ID
	@Override
	public Recipe findRecipeById(long id) {
		//Get Recipe ID from parameter
		Optional<Recipe> recipeDb = this.recipeRepository.findById(id);
		//If Recipe ID is found
		if(recipeDb.isPresent()) {
			return recipeDb.get();
		}else {
			throw new ResourceNotFoundException("Recipe not found with id:"+id);
		}
	}

	//Get recipe by name
	@Override
	public Recipe findRecipeByName(String name) {
		return this.recipeRepository.findRecipeByName(name);
	}
	
	//Get recipe ingredients by name
	@Override
	public RecipeIngredients getRecipeIngredientsByName(String name) {
		
		RecipeIngredients ingredientsForRecipe = sql.getRecipeIngredients(name);
		
		return ingredientsForRecipe;
		
	}


	//Find all recipes
	@Override
	public List<Recipe> findAllRecipes() {
		return this.recipeRepository.findAll();
	}

	
	
	

}
