package com.wurst.curry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wurst.curry.model.Worker;


public interface WorkerRepository extends JpaRepository<Worker, Long>{

	//Get all available workers
	List<Worker> findByavailableIs(boolean available);
	
	//Get workers list from busiest to less busy
	List<Worker> findAllByOrderByTimeBusyDesc();
	
	
}
