package com.wurst.curry.repository;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wurst.curry.model.IngredientsStorage;
import com.wurst.curry.model.IngredientsStorageKey;


public interface IngredientsStorageRepository  extends JpaRepository<IngredientsStorage, IngredientsStorageKey> {
	
	//Get ingredients given a recipe ID
	ArrayList<IngredientsStorage> findIngredientNameByRecipeId(long id);

	//Get ingredient by name
	IngredientsStorage findByingredientName(String name);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM INGREDIENTS_STORAGE "
			+ " WHERE RECIPE_ID = :recipe_id", nativeQuery = true)
	void deleteStorageRecipe(@Param("recipe_id") Integer recipeId);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE INGREDIENTS_STORAGE "
			+ " SET QUANTITY_NEEDED = :quantity "
			+ " WHERE INGREDIENT_NAME = :ingredientName AND RECIPE_ID = :recipeId", nativeQuery = true)
	void modifyStorageIngredient(@Param("recipeId")Integer recipeId, @Param("ingredientName")String ingredientName,@Param("quantity") Integer quantity);
	
	
}