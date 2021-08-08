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

import com.wurst.curry.model.MenuItem;
import com.wurst.curry.service.MenuItemService;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/curryWurst")
public class MenuItemController {

	@Autowired
	private MenuItemService menuItemService;
	
	
	// Add menu item
	@PostMapping("/menu")
	ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem menuItem) {
		return ResponseEntity.ok().body(this.menuItemService.addMenuItem(menuItem)); 
	};
	
	//Update menu item
	@PutMapping("/menu/{id}")
	public ResponseEntity<MenuItem> updateMenuItem(@PathVariable long id,@RequestBody MenuItem menuItem){
		menuItem.setId(id);
		return ResponseEntity.ok().body(this.menuItemService.updateMenuItem(menuItem));
	}
	
	//Delete Menu item
	@DeleteMapping("menu/{id}")
	public HttpStatus deleteMenuItem(@PathVariable long id) {
		this.menuItemService.deleteMenuItem(id);
		return HttpStatus.OK;
	}
	
	
	//List all menu items
	@GetMapping("/menu")
	ResponseEntity<List<MenuItem>> findAllMenuItem(){
		return ResponseEntity.ok(this.menuItemService.findAllMenuItem());
	}
	
	//Find all available menu items
	List<MenuItem> findAllAvailableMenuItems(){
		return null;
	}
	

	
}
