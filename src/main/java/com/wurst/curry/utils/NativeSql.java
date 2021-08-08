package com.wurst.curry.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wurst.curry.sqlObjects.MissingIngredients;
import com.wurst.curry.sqlObjects.RecipeIngredients;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NativeSql {
	
	
	@Autowired
    private EntityManagerFactory entityManagerFactory;
	
	//Declaring query to find missing ingredients from recipes of the order
	private static final String recipeQuery = "SELECT STORAGE.FOOD_ITEM_NAME as name,  "
			+ "CAST(STORAGE .AVAILABLE_UNITS AS INT), CAST(INGREDIENTS_STORAGE.QUANTITY_NEEDED AS INT),"
			+ "CAST(WAREHOUSE.DELIVERY_TIME AS INT), CAST((QUANTITY_NEEDED-AVAILABLE_UNITS) AS INT) AS UNITS_MISSING"
			+ " FROM STORAGE "
			+ " INNER JOIN INGREDIENTS_STORAGE "
			+ " ON STORAGE .FOOD_ITEM_NAME = INGREDIENTS_STORAGE.INGREDIENT_NAME "
			+ " INNER JOIN WAREHOUSE "
			+ " ON STORAGE.FOOD_ITEM_NAME = WAREHOUSE.NAME "
			+ " WHERE AVAILABLE_UNITS < QUANTITY_NEEDED AND RECIPE_ID = :id "
			+ "ORDER BY DELIVERY_TIME DESC";
	
	//Declaring query to find missing Items from the order
	private static final String itemQuery = 
			"SELECT STORAGE.FOOD_ITEM_NAME as name,"
			+ "	CAST(STORAGE.AVAILABLE_UNITS AS INT), CAST(INGREDIENTS_STORAGE.QUANTITY_NEEDED AS INT),"
			+ "	CAST(WAREHOUSE.DELIVERY_TIME AS INT),CAST((:quantityN - AVAILABLE_UNITS) AS INT) AS UNITS_MISSING"
			+ "	FROM STORAGE"
			+ "	INNER JOIN INGREDIENTS_STORAGE"
			+ "	ON STORAGE .FOOD_ITEM_NAME = INGREDIENTS_STORAGE.INGREDIENT_NAME"
			+ "	INNER JOIN WAREHOUSE"
			+ "	ON STORAGE.FOOD_ITEM_NAME = WAREHOUSE.NAME"
			+ "	WHERE AVAILABLE_UNITS < :quantityN AND RECIPE_ID = :recipeId"
			+ "	ORDER BY DELIVERY_TIME DESC";
	

	//Declaring query for order Items by iD
	private static final String currentOrderItemsQuery = "SELECT * FROM ORDERS_ORDER_ITEMS WHERE ORDERS_ID = :id";
	
	//Declaring query to find ingredients by recipe ID or name
	private static final String getRecipeIngredients = "SELECT INGREDIENT_NAME, "
			+ " NAME, QUANTITY_NEEDED FROM INGREDIENTS_STORAGE "
			+ " INNER JOIN RECIPE "
			+ " ON RECIPE.ID = INGREDIENTS_STORAGE .RECIPE_ID "
			+ " WHERE NAME = :recipeName";
	
	//Declaring query to find ingredients by recipe ID or name
	private static final String addNewIngredient = "INSERT INTO INGREDIENTS_STORAGE "
			+ "VALUES(:name,:id,:quantity)";
	
	
	
	//Method firm for when the item to look for is a recipe
	public List<MissingIngredients> filterNeededIngredients (int recipeId) {
		EntityManager session = entityManagerFactory.createEntityManager();
        try {
        	List<Object[]> query = (List<Object[]>) session.createNativeQuery(this.recipeQuery)
        			.setParameter("id", recipeId)
        			.getResultList();
        	
        	List<MissingIngredients> result = new ArrayList<>();
            for (Object[] row : query) {
                result.add(new MissingIngredients(row));
            }
            return result;
    
        }
        catch (NoResultException e){
            return null;
        }
        finally {
            if(session.isOpen()) session.close();
        }
		
	}
	
	//Method firm for when the item to look for is a made item (cocacola, brownie,etc)
	public List<MissingIngredients> filterNeededIngredients (int recipeId, int quantityN) {
		EntityManager session = entityManagerFactory.createEntityManager();
		
        try {
        	List<Object[]> query = (List<Object[]>) session.createNativeQuery(this.itemQuery)
        			.setParameter("recipeId", recipeId)
        			.setParameter("quantityN", quantityN)      
        			.getResultList();
        	
        	List<MissingIngredients> result = new ArrayList<>();
            for (Object[] row : query) {
                result.add(new MissingIngredients(row));
            }
            return result;
    
        }
        catch (NoResultException e){
            return null;
        }
        finally {
            if(session.isOpen()) session.close();
        }
		
	}

	public List<String> getOrderItemsById(String orderId){
		EntityManager session = entityManagerFactory.createEntityManager();
        try {
        	List<Object[]> query = (List<Object[]>) session.createNativeQuery(this.currentOrderItemsQuery)
        			.setParameter("id", orderId)
        			.getResultList();
        	
        	List<String> result = new ArrayList<>();
            for (Object[] row : query) {
                result.add((String) row[1]);
            }
            return result;
    
        }
        catch (NoResultException e){
            return null;
        }
        finally {
            if(session.isOpen()) session.close();
        }
		
	}
		
	public RecipeIngredients getRecipeIngredients (String recipeName) {
		EntityManager session = entityManagerFactory.createEntityManager();
		
        try {
        	List<Object[]> query = (List<Object[]>) session.createNativeQuery(this.getRecipeIngredients)
        			.setParameter("recipeName", recipeName)    
        			.getResultList();
        	
        	
        	
        	List<HashMap<String,Integer>>IngredientsList = new ArrayList<HashMap<String,Integer>>();
        	RecipeIngredients result = new RecipeIngredients();
        	
            for (Object[] row : query) {
            	HashMap<String,Integer> ingredientRow = new HashMap<String,Integer>();
            	ingredientRow.put((String) row[0], (int) row[2]);
            	IngredientsList.add(ingredientRow);
            }
            
            result.setRecipeName(recipeName);
            result.setIngredientsList(IngredientsList);
            
            return result;
    
        }
        catch (NoResultException e){
            return null;
        }
        finally {
            if(session.isOpen()) session.close();
        }
		
	}
	
	public List<Object[]>  AddNewIngredient(String name,String id, int quantity){
		EntityManager session = entityManagerFactory.createEntityManager();
        try {
        	List<Object[]> query = (List<Object[]>) session.createNativeQuery(this.addNewIngredient)
        			.setParameter("name", name)
        			.setParameter("id", id)
        			.setParameter("quantity", quantity)
        			.getResultList();
        	
//        	List<String> result = new ArrayList<>();
//            for (Object[] row : query) {
//                result.add((String) row[1]);
//            }
        	log.info(query.toString());        
        	return query;
    
        }
        catch (NoResultException e){
            return null;
        }
        finally {
            if(session.isOpen()) session.close();
        }
		
	}
}
