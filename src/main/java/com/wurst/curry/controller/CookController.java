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

import com.wurst.curry.model.Cook;
import com.wurst.curry.service.CookService;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/curryWurst")
public class CookController {
	
	@Autowired
	private CookService cookService;
	
	
	//Create new cook
	@PostMapping("/cooks")
	public ResponseEntity<Cook> addCook(@RequestBody Cook cook){
		return ResponseEntity.ok().body(this.cookService.addCook(cook));
	}
	
	//Update cook
	@PutMapping("/cooks/{id}")
	ResponseEntity<Cook> updateCook(@PathVariable long id,@RequestBody Cook cook){
		cook.setId(id);
		return ResponseEntity.ok().body(this.cookService.updateCook(cook));	
	}
	
	//Delete cook by id
	@DeleteMapping("/cooks/{id}")
	public HttpStatus deleteCook(@PathVariable long id ){
		this.cookService.deleteCook(id);
		return HttpStatus.OK;
	}
	
	//List all cooks
	@GetMapping("/cooks")
	public ResponseEntity<List<Cook>> findAllCooks(){
		return ResponseEntity.ok(this.cookService.findAllCooks());
	}

	
	// Find cook by id
	@GetMapping("/cooks/{id}")
	public ResponseEntity<Cook> getCookById(@PathVariable long id){
		return ResponseEntity.ok(this.cookService.findCookById(id));
	}
	
	
	//Find all available cooks
	@GetMapping("/cooks/available")
	ResponseEntity<List<Cook>> findAllAvailableCooks(){
		return ResponseEntity.ok(this.cookService.findAllAvailableCooks());
	}

}
