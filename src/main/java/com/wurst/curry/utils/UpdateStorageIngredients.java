package com.wurst.curry.utils;


import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wurst.curry.model.Storage;
import com.wurst.curry.service.StorageService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Component
public class UpdateStorageIngredients extends TimerTask{
	//This class is made to trigger a task given a specific date time moment, where the ingredient
	//should increase or decrease

	
	
	@Autowired
	private StorageService storageService;
	
	private String operation; //increase or decrease
	private String name; 
	private int units; 
	
	
	public UpdateStorageIngredients() {
		
	}
	
	
	public UpdateStorageIngredients(String operation, String name, int units) {
		super();
		this.operation = operation;
		this.name = name;
		this.units = units;
	}

	
	//Increase given ingredient in the storage table
	public void increaseIngredient (String name, int units) {
		Storage storageIngredientDb = this.storageService.findByName(name);
		
		Storage updateIngredient = storageIngredientDb;
		//Define quantity to increase
		int available = updateIngredient.getAvailableUnits();
		available+=units;
		//Update ingredient in the database
		updateIngredient.setAvailableUnits(available);
		this.storageService.updateFoodItem(updateIngredient);
	
	}

	//Decrease given ingredient in the storage table
	public void decreaseIngredient (String name, int units) {
		Storage storageIngredientDb = this.storageService.findByName(name);
		
		Storage upgradeIngredient = storageIngredientDb;
		//Define quantity to decrease
		int available = upgradeIngredient.getAvailableUnits();
		
		if((available > units)) {
			available-=units;	
		}else {
			available=0;
		}
		
		//Update ingredient in the database
		upgradeIngredient.setAvailableUnits(available);
		this.storageService.updateFoodItem(upgradeIngredient);
	}

	
	@Override
	public void run() {
		switch (this.operation) {
			case "increase":
				increaseIngredient (this.name, this.units);
			break;
			
			case "decrease":
				decreaseIngredient(this.name, this.units);
			break;
		}
	}
}
