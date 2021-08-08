package com.wurst.curry.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.wurst.curry.model.Cook;
import com.wurst.curry.model.Worker;

public interface ScheduleService {
	
	HashMap<String,String> scheduleWorkers(Entry<String, Integer> m , int recipeId,String orderId);
	
	HashMap<String, String> scheduleCooks(HashMap<String, Integer> items, String orderId, Integer workersTime);
	
	List<Worker> setWorkersNTA();
	
	List<Cook> setCooksNTA ();

}
