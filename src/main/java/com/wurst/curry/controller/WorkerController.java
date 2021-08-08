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

import com.wurst.curry.model.Worker;
import com.wurst.curry.service.WorkerService;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/curryWurst")
public class WorkerController {
	
	@Autowired
	private WorkerService workerService;
	
	//Create a new worker
	@PostMapping("/workers")
	public ResponseEntity<Worker> addRecipe(@RequestBody Worker worker){
		return ResponseEntity.ok().body(this.workerService.addWorker(worker));
	}
	
	//Update worker
	@PutMapping("/workers/{id}")
	ResponseEntity<Worker> updateWorker(@RequestBody Worker worker,@PathVariable long id){
		worker.setId(id);
		return ResponseEntity.ok().body(this.workerService.updateWorker(worker));
	}
	
	//Delete workers
	@DeleteMapping("/workers/{id}")
	public HttpStatus deleteRecipe(@PathVariable long id) {
		this.workerService.deleteWorker(id);
		return HttpStatus.OK;
	}
	
	
	//List all workers
	@GetMapping("/workers")
	ResponseEntity<List<Worker>> findAllWorkers(){
		return ResponseEntity.ok(this.workerService.findAllWorkers());
	}
	
	//List all available workers
	@GetMapping("/workers/available")
	ResponseEntity<List<Worker>> findAllAvailableWorkers(){
		return ResponseEntity.ok(this.workerService.findAllAvailableWorkers());
	}
	
}
