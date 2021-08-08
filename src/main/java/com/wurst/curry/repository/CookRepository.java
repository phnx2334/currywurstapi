package com.wurst.curry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wurst.curry.model.Cook;

public interface CookRepository extends JpaRepository<Cook, Long>{

	//Get all available cooks
	List<Cook> findAllByOrderByAvailableDesc();
	
	//Get cooks list from busiest to less busy
	List<Cook> findAllByOrderByTimeBusyDesc();
}
