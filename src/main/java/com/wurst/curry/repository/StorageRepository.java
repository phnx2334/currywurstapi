package com.wurst.curry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wurst.curry.model.Storage;

public interface StorageRepository extends JpaRepository<Storage, String>{
	
	//Get all available ingredients
	List<Storage> findByavailableUnitsGreaterThan(int units);
	
	//Find ingredient by name
	Storage findByName(String name);
	
	//Find if ingredient is available
	Storage findByNameAndAvailableUnits(String name, int availableUnits );

}
