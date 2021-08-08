package com.wurst.curry.model;

import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;



@Data
@Entity
@Table(name="worker")
public class Worker {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name="available", columnDefinition="boolean default true")
	private boolean available;
	
	@Column(name="capacity")
	private int capacity;
	
	//List of carried items
	@Column(name="carried_items", nullable = true, columnDefinition = "LONGTEXT")
	private HashMap<String,String> items;
	
	
	@Column(name="NTA", nullable = true) // Next time available
	private String NTA;
	
	@Column(name="name")
	private String name;
	
	@Column(name="time_busy")
	private int timeBusy;
	

	
	
}