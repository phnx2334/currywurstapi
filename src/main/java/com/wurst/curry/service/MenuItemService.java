package com.wurst.curry.service;

import java.util.List;

import com.wurst.curry.model.MenuItem;

public interface MenuItemService {
	
	// Add menu item
	MenuItem addMenuItem(MenuItem menuItem);
	
	//Update menu item
	MenuItem updateMenuItem(MenuItem menuItem);
	
	//Delete Menu item
	void deleteMenuItem(long itemId);
	
	//Get menu item by ID
	MenuItem findMenuItemById(long itemId);
	
	//Get menu item by name
	MenuItem findMenuItemByName(String itemName);

	//List all menu items
	List<MenuItem> findAllMenuItem();


}
