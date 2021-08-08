package com.wurst.curry.sqlObjects;


import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;
import lombok.Data;


//Class used to map the queries declared in nativeSQL used to find the ingredients 
//missing for the order

@Data
@Component
public class RecipeIngredients {
	String recipeName;
	List<HashMap<String,Integer>>IngredientsList;	
	
	public RecipeIngredients() {
       
    }
}
