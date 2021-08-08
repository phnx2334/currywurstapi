package com.wurst.curry.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wurst.curry.model.MenuItem;


public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
	
	Optional<MenuItem> findMenuItemByName(String name);


}
