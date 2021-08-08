package com.wurst.curry.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class IngredientsStorageKey implements Serializable {
	
	
	//Composite key for ingredients in the storage
	/**
	 * 
	 */
	private static final long serialVersionUID = -3568271710759842790L;


	@Column(name = "recipe_id")
	long recipeId;
	

	@Column(name = "ingredient_name")
	String ingredientName;

}
