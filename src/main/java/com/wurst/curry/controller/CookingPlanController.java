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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wurst.curry.model.CookingPlan;
import com.wurst.curry.service.CookingPlanService;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/curryWurst")
public class CookingPlanController {
	
	@Autowired
	CookingPlanService cookingPlanService;
	
	//Get the cooking plan
	@GetMapping("/cookingPlan")
	public ResponseEntity<List<CookingPlan>> getCookingPlan(){
		return ResponseEntity.ok(this.cookingPlanService.getCookingPlan());
	}
	
	//Add entry to the cooking plan
	@PostMapping("/cookingPlan")
	public ResponseEntity<CookingPlan> addEntry(CookingPlan entry) {
		return ResponseEntity.ok(this.cookingPlanService.addEntry(entry));
	}
	
	//Update entry in the cooking plan TODO
	@PostMapping("/cookingPlan/{id}")
	public ResponseEntity<CookingPlan> updateEntry(@PathVariable String id, @RequestBody CookingPlan cookingPlanEntry){
			cookingPlanEntry.setId(id);
			return ResponseEntity.ok().body(this.cookingPlanService.updateEntry(cookingPlanEntry));
	}
		
	//Delete entry in the cooking plan
	@DeleteMapping("/cookingPlan/{id}")
	public HttpStatus deleteCookingPlanEntry(@PathVariable String id ){
		this.cookingPlanService.deleteEntry(id);
		return HttpStatus.OK;
	}
	
	//Return the cooking plan for a specific order
	@GetMapping("/cookingPlan/{id}")
	public ResponseEntity<CookingPlan> getCookingPlan(@PathVariable String id ){
		return ResponseEntity.ok(this.cookingPlanService.getCookingPlanById(id));
	}
}
