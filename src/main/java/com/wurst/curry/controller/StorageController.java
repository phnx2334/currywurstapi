package com.wurst.curry.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wurst.curry.model.Storage;
import com.wurst.curry.service.StorageService;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/curryWurst")
public class StorageController {
	
	@Autowired
	StorageService storageService;
	
	//Add ingredient
	@PostMapping("/storage")
	ResponseEntity<Storage> addIngredient(@RequestBody Storage ingredient){
		return ResponseEntity.ok().body(this.storageService.addFoodItem(ingredient));
	}
	
	//Update ingredients
	@PutMapping("/storage")
	ResponseEntity<Storage> updateIngredient(@RequestBody Storage ingredient){
		return ResponseEntity.ok().body(this.storageService.updateFoodItem(ingredient));
	}
	
	//Delete ingredient
	@DeleteMapping("/storage/{name}")	
	public HttpStatus deleteIngredient(@PathVariable String name){
		this.storageService.deleteFoodItem(name);
		return HttpStatus.OK;
	}
	
	//List all ingredients
	@GetMapping("/storage")
	ResponseEntity<List<Storage>> findAllIngredients(){
		return ResponseEntity.ok(this.storageService.findAllFoodItems());
	}
	
	//List all ingredients that are available
	@GetMapping("/storage/available")
	ResponseEntity<List<Storage>> findAllAvailableIngredients(){
		return ResponseEntity.ok(this.storageService.findAllAvailableFoodItems());
	}
	
	//List all ingredients that are available
	@GetMapping("/storage/{name}")
	ResponseEntity<Storage> findIngredientByName(@PathVariable String name){
		if(this.storageService.findByName(name)!=null) {
			return ResponseEntity.ok().body(this.storageService.findByName(name));
		}else {
			return ((BodyBuilder) ResponseEntity.notFound()).body(this.storageService.findByName(name));
		}
		
	}

}
