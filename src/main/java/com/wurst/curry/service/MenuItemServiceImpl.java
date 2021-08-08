package com.wurst.curry.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wurst.curry.model.MenuItem;
import com.wurst.curry.repository.MenuItemRepository;
import com.wurst.curry.exception.ResourceNotFoundException;

@Component
@Service
@Transactional
public class MenuItemServiceImpl implements MenuItemService{

	@Autowired
	MenuItemRepository menuItemRepository;
	
	//Add new menu item
	@Override
	public MenuItem addMenuItem(MenuItem menuItem) {
		return menuItemRepository.save(menuItem);
	}
	
	//Update menu Item
	@Override
	public MenuItem updateMenuItem(MenuItem menuItem) {
		Optional<MenuItem> menuItemDb = this.menuItemRepository.findById(menuItem.getId());
		
		if(menuItemDb.isPresent()) {
			MenuItem menuItemUpdate = menuItemDb.get();
			
			menuItemUpdate.setId(menuItem.getId());
			menuItemUpdate.setName(menuItem.getName());
			menuItemUpdate.setPrice(menuItem.getPrice());
			this.menuItemRepository.save(menuItemUpdate);
			
			return menuItemUpdate;
		}else {//Throw exception if not found
			throw new ResourceNotFoundException("Menu Item not found with id:"+menuItem.getId());
		}	
		
	}
	
	//Delete menu item
	@Override
	public void deleteMenuItem(long itemId) {
		Optional<MenuItem> menuItemDb = this.menuItemRepository.findById(itemId);
		
		if(menuItemDb.isPresent()) {
			this.menuItemRepository.delete(menuItemDb.get());
		}else {
			throw new ResourceNotFoundException("There is no menu item with id "+itemId);
		}
		
	}
	
	// Find menu Item by ID
	@Override
	public MenuItem findMenuItemById(long itemId) {
		//Get product ID from parameter
		Optional<MenuItem> menuItemDb = this.menuItemRepository.findById(itemId);
		//If product ID is found
		if(menuItemDb.isPresent()) {
			return menuItemDb.get();
		}else {
			throw new ResourceNotFoundException("Menu Item not found with id:"+itemId);
		}
	}
	
	//Get menu item by name
	@Override
	public MenuItem findMenuItemByName(String itemName) {
		//Get product ID from parameter
		Optional<MenuItem> menuItemDb = this.menuItemRepository.findMenuItemByName(itemName);
		//If product ID is found
		if(menuItemDb.isPresent()) {
			return menuItemDb.get();
		}else {
			throw new ResourceNotFoundException("Menu Item not found with name:"+ itemName);
		}
	}

	
	//List all items in the menu
	@Override
	public List<MenuItem> findAllMenuItem() {
		return this.menuItemRepository.findAll();
	}


}

