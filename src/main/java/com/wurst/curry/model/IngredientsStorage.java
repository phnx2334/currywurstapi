package com.wurst.curry.model;


import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;


@Data
@Entity
public class IngredientsStorage {

	@EmbeddedId
	IngredientsStorageKey id;
	
	@ManyToOne
	@MapsId("recipeId")
	@JoinColumn(name = "recipe_id", nullable = true)
	Recipe recipe;
	
	@ManyToOne
	@MapsId("ingredientName")
	@JoinColumn(name = "ingredient_name")
	Storage ingredient;
	
	//Quantity of the ingredient needed for the recipe
	int quantityNeeded;
}
