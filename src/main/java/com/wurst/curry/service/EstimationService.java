package com.wurst.curry.service;

import java.util.HashMap;
import java.util.Map.Entry;

public interface EstimationService {
	
	String estimateOrder(HashMap <String,Integer> items);
	
	int estimateWorkers(Entry<String, Integer> m , int recipeId);
	
	int estimateCooks(HashMap <String,Integer> items);

}
