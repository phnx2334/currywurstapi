package com.wurst.curry.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.wurst.curry.model.Recipe;
import com.wurst.curry.service.RecipeService;
import com.wurst.curry.sqlObjects.RecipeIngredients;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/curryWurst")
public class RecipeController {

	@Autowired
	private RecipeService recipeService;
	
	//Add recipe
	@PostMapping("/recipes")
	public ResponseEntity<Recipe> addRecipe(@RequestBody Recipe recipe){
		return ResponseEntity.ok().body(this.recipeService.addRecipe(recipe));
	}

	//Update a recipe
	@PutMapping("/recipes/{id}")
	ResponseEntity<Recipe> updateRecipe(@PathVariable long id,@RequestBody Recipe recipe){
		recipe.setId(id);
		return ResponseEntity.ok().body(this.recipeService.updateRecipe(recipe));
	}
	
	//Delete a recipe
	@DeleteMapping("/recipes/{id}")
	public HttpStatus deleteRecipe(@PathVariable long id) {
		this.recipeService.deleteRecipe(id);
		return HttpStatus.OK;
	}
	
	//Display all recipes
	@GetMapping  ("/recipes")
	ResponseEntity<List<Recipe>>findAllRecipes(){
		return ResponseEntity.ok(this.recipeService.findAllRecipes());
	}
	
	
	//Get recipe by ID
	@GetMapping  ("/recipes/{id}")
	ResponseEntity<Recipe> findRecipeById(@PathVariable long id){
		return ResponseEntity.ok().body(this.recipeService.findRecipeById(id));
	}
	
	//Get recipe ingredients by name
	@GetMapping  ("/recipes/ingredients/{name}")
	ResponseEntity<RecipeIngredients> getRecipeIngredientsByName(@PathVariable String name){
		return ResponseEntity.ok().body(this.recipeService.getRecipeIngredientsByName(name));
	}
	
	
	//Get recipe by name
	@GetMapping  ("/recipes/{name}")
	ResponseEntity<Recipe> findRecipeByName(@PathVariable String name){
		return ResponseEntity.ok().body(this.recipeService.findRecipeByName(name));
	}

}
