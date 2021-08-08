package com.wurst.curry.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wurst.curry.exception.ResourceNotFoundException;
import com.wurst.curry.model.Storage;
import com.wurst.curry.repository.StorageRepository;


@Service
@Transactional
public class StorageServiceImpl implements StorageService {
	
	@Autowired
	StorageRepository storageRepository;

	//Add food item
	@Override
	public Storage addFoodItem(Storage foodItem) {
		return this.storageRepository.save(foodItem);
	}
	
	//Update food item
	@Override
	public Storage updateFoodItem(Storage foodItem) {
		Storage foodItemDb = this.storageRepository.findByName(foodItem.getName());
		
		if(!(foodItemDb == null)) {
			Storage foodItemUpdate = foodItemDb;
			foodItemUpdate.setAvailableUnits(foodItem.getAvailableUnits());
			this.storageRepository.save(foodItemUpdate);
			
			return foodItemUpdate;
		}else {//Throw exception if not found
			throw new ResourceNotFoundException("Ingredient not found with name: "+foodItem.getName());
		}	
	}

	//Delete food item
	@Override
	public void deleteFoodItem(String name) {
		Storage storageDb = this.storageRepository.findByName(name);
		
		if(!(storageDb == null)) {
			this.storageRepository.delete(storageDb);
		}else {
			throw new ResourceNotFoundException("There is no ingredient with the name of "+name);
		}
		
		
	}

	//Get all food items
	@Override
	public List<Storage> findAllFoodItems() {
		return this.storageRepository.findAll();
	}

	//Find all food items that are available
	@Override
	public List<Storage> findAllAvailableFoodItems() {
		return this.storageRepository.findByavailableUnitsGreaterThan(0);
	}

	//Find food item by name
	@Override
	public Storage findByName(String name) {
		Storage storageDb = this.storageRepository.findByName(name);
		if(!(storageDb == null)) {
			return this.storageRepository.findByName(name);
		}else {
			throw new ResourceNotFoundException("There is no ingredient with the name of "+name);
		}
	}

	


}
