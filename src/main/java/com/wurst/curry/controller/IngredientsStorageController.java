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

import com.wurst.curry.model.IngredientsStorage;

import com.wurst.curry.service.IngredientsStorageService;


@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/curryWurst")
public class IngredientsStorageController {

	
	@Autowired
	IngredientsStorageService ingredientsStorageService;
	
	//Add ingredient
	@PostMapping("/storage/ingredients")
	ResponseEntity<IngredientsStorage> addIngredient(@RequestBody IngredientsStorage ingredient){
		return ResponseEntity.ok().body(this.ingredientsStorageService.addIngredient(ingredient));
	}
	
	//Find ingredient all ingredients in the table
	@GetMapping("/storage/ingredients")
	ResponseEntity<List<IngredientsStorage>> findAllIngredients(){
		return ResponseEntity.ok(this.ingredientsStorageService.findAllIngredients());
	}
	
	//Modify ingredient quantity  on the table
	@PutMapping("/storage/ingredients/{recipeId}/{ingredientName}/{quantity}")
	public HttpStatus modifyQuantityNeeded(@PathVariable Integer recipeId, @PathVariable String ingredientName, @PathVariable Integer quantity ) {
		this.ingredientsStorageService.changeIngredientQuantity(recipeId, ingredientName, quantity);
		return HttpStatus.OK;
	}
	
	//Delete recipe from table
	@DeleteMapping("/storage/ingredients/{recipeId}")
	public HttpStatus deleteStorageRecipe(@PathVariable Integer recipeId) {
		this.ingredientsStorageService.deleteStorageRecipe(recipeId);
		return HttpStatus.OK;
	}
}
