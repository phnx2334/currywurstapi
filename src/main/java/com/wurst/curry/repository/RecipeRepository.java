package com.wurst.curry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wurst.curry.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
	
	// Find recipe by name
	Recipe findRecipeByName(String name);

}
