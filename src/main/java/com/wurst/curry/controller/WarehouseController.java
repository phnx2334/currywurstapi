package com.wurst.curry.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wurst.curry.model.Warehouse;
import com.wurst.curry.service.WarehouseService;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/curryWurst")
public class WarehouseController {
	
	@Autowired
	private WarehouseService warehouseService;
	
	//Add a new ingredient
	@PostMapping("/warehouse")
	ResponseEntity<Warehouse> addIngredient(@RequestBody Warehouse ingredient){
		return ResponseEntity.ok().body(this.warehouseService.addIngredient(ingredient));
	}
	
	//Update ingredient
	@PutMapping("/warehouse/{name}")
	ResponseEntity<Warehouse> updateIngredient(@PathVariable String name, @RequestBody Warehouse ingredient){
		ingredient.setName(name);
		return ResponseEntity.ok().body(this.warehouseService.updateIngredient(ingredient));
	}
	
	
	//Delete ingredient
	@DeleteMapping("/warehouse/{id}")	
	public HttpStatus deleteIngredient(@PathVariable long id ){
		this.warehouseService.deleteIngredient(id);
		return HttpStatus.OK;
	}
	
	//List all registered ingredients
	@GetMapping("/warehouse")
	ResponseEntity<List<Warehouse>> findAllIngredients(){
		return ResponseEntity.ok(this.warehouseService.findAllIngredients());
	}

}
