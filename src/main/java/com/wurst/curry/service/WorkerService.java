package com.wurst.curry.service;

import java.util.List;

import com.wurst.curry.model.Worker;


public interface WorkerService {
	//Create a new worker
	Worker addWorker(Worker worker);
	
	//Update worker
	Worker updateWorker(Worker worker);
	
	//Delete workers
	void deleteWorker(long workerId);
	
	//List all workers
	List<Worker> findAllWorkers();
	
	//Find worker by ID
	Worker findWorkerById(long workerId);
	
	//List all available workers
	List<Worker> findAllAvailableWorkers();
	
	//Add to carried Items list
	void addCarriedItem(long workerId, String item, String orderId);
	
	//Add busy time
	void addBusyTime(long id, long time);
	
	//Get workers list from busiest to less busy
	List<Worker> findAllByOrderByTimeBusyDesc();
}
