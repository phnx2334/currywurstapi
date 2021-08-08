package com.wurst.curry.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wurst.curry.exception.ResourceNotFoundException;
import com.wurst.curry.model.Worker;
import com.wurst.curry.repository.WorkerRepository;


@Service
@Transactional
public class WorkerServiceImpl implements WorkerService {

	@Autowired
	private WorkerRepository workerRepository;
	
	//Add a worker
	@Override
	public Worker addWorker(Worker worker) {
		return workerRepository.save(worker);
	}
	
	//Update a worker
	@Override
	public Worker updateWorker(Worker worker) {
		Optional<Worker> workerDb = this.workerRepository.findById(worker.getId());
		
		if(workerDb.isPresent()) {
			Worker workerUpdate = workerDb.get();
			workerUpdate.setId(worker.getId());
			workerUpdate.setNTA(worker.getNTA());
			workerUpdate.setName(worker.getName());
			workerUpdate.setItems(worker.getItems());
			workerUpdate.setTimeBusy(worker.getTimeBusy());
			workerUpdate.setAvailable(worker.isAvailable());
			workerUpdate.setCapacity(worker.getCapacity());
			this.workerRepository.save(workerUpdate);
			return workerUpdate;
		}else {//Throw exception if not found
			throw new ResourceNotFoundException("Worker not found with id:"+worker.getId());
		}
	}

	//Delete a worker
	@Override
	public void deleteWorker(long workerId) {
		Optional<Worker> workerDb = this.workerRepository.findById(workerId);
		
		if(workerDb.isPresent()) {
			this.workerRepository.delete(workerDb.get());
		}else {
			throw new ResourceNotFoundException("There is no worker with id "+workerId);
		}
		
	}
	
	//Find all workers
	@Override
	public List<Worker> findAllWorkers() {
		return this.workerRepository.findAll();
	}
	
	
	//Find worker by ID
	@Override
	public Worker findWorkerById(long workerId) {
		//Get cook ID from parameter
		Optional<Worker> workerDb = this.workerRepository.findById(workerId);
		//If cook ID is found
		if(workerDb.isPresent()) {
			return workerDb.get();
		}else {
			throw new ResourceNotFoundException("Worker not found with id:"+workerId);
		}
	}


	
	//Find all available workers
	@Override
	public List<Worker> findAllAvailableWorkers() {
		return workerRepository.findAllByOrderByTimeBusyDesc();
	}
	
	//Add to carried Items list
	public void addCarriedItem(long workerId, String item, String orderId){
		Optional<Worker> workerDb = this.workerRepository.findById(workerId);
		
		if(workerDb.isPresent()) {
			Worker updateWorker = workerDb.get();
			//Get already carried items
			HashMap<String, String> currList = updateWorker.getItems();
			
			String items = "";
			
			if(!(currList == null)) {
				//Evaluate if key for order already exists
				if(currList.containsKey(orderId)) {
					items = currList.get(orderId);
					//Add new recipe
					items = items + ","+item;
					//Delete the first comma
					if(items.substring(0, 1) == ",") {
						items = items.substring(1);
					}
					currList.put(orderId, items);
				}else {//If not, add a new order with the recipe name
					currList.put(orderId, item);
				}
			}else {//If is empty, create a new list with order id and recipe name
				currList = new HashMap<String,String>();
				currList.put(orderId, item);
			}
			
			
			updateWorker.setItems(currList);
			updateWorker(updateWorker);
			
		}else {
			throw new ResourceNotFoundException("There is no worker with id "+workerId);
		}
		
	}

	//Add busy time
	public void addBusyTime(long id, long time){
		Optional<Worker> workerDb = this.workerRepository.findById(id);
		
		if(workerDb.isPresent()) {
			Worker updateWorker = workerDb.get();
			int currTime = updateWorker.getTimeBusy();
			currTime+=time;
			updateWorker.setTimeBusy(currTime);
			updateWorker.setAvailable(false);
			updateWorker(updateWorker);
			
		}else {
			throw new ResourceNotFoundException("There is no worker with id "+id);
		}
	}
	
	//Get workers list from busiest to less busy
	public List<Worker> findAllByOrderByTimeBusyDesc(){
		return this.workerRepository.findAllByOrderByTimeBusyDesc();
	}

	
}
