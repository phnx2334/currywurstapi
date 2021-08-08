package com.wurst.curry.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wurst.curry.exception.ResourceNotFoundException;
import com.wurst.curry.model.Warehouse;
import com.wurst.curry.repository.WarehouseRepository;


@Service
@Transactional
public class WarehouseServiceImpl implements WarehouseService {
	
	@Autowired
	WarehouseRepository warehouseRepository;

	//Add a new ingredient
	@Override
	public Warehouse addIngredient(Warehouse ingredient) {
		return this.warehouseRepository.save(ingredient);
	}
	
	//Update Ingredient
	@Override
	public Warehouse updateIngredient(Warehouse ingredient) {
		Optional<Warehouse> ingredientDb = this.warehouseRepository.findWarehouseByName(ingredient.getName());
		
		if(ingredientDb.isPresent()) {
			Warehouse ingredientUpdate = ingredientDb.get();
			ingredientUpdate.setName(ingredient.getName());
			this.warehouseRepository.save(ingredientUpdate);
			
			return ingredientUpdate;
		}else {//Throw exception if not found
			throw new ResourceNotFoundException("Ingredient from the warehouse not found with id:"+ingredient.getName());
		}
	}
	
	//Delete ingredient
	@Override
	public void deleteIngredient(long ingredientId) {
		
		Optional<Warehouse> ingredientDb = this.warehouseRepository.findById(ingredientId);
		
		if(ingredientDb.isPresent()) {
			this.warehouseRepository.delete(ingredientDb.get());
		}else {
			throw new ResourceNotFoundException("There is no ingredient with id "+ingredientId);
		}
	}
	
	//List all registered ingredients
	@Override
	public List<Warehouse> findAllIngredients() {
		return this.warehouseRepository.findAll();
	}
	
	//Find ingredient by name
	@Override
	public Warehouse findIngredientByName(String name) {
		//Get ingredient ID from parameter
			Optional<Warehouse> ingredientDb = this.warehouseRepository.findWarehouseByName(name);
			//If ingredient ID is found
			if(ingredientDb.isPresent()) {
				return ingredientDb.get();
			}else {
				throw new ResourceNotFoundException("Ingredient not found with name :"+name);
			}
	}
	

}
