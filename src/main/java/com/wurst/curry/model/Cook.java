package com.wurst.curry.model;

import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;


@JsonPropertyOrder({"id","name","recipesToCook", "available","nta", "timeBusy"})
@Data
@Table(name="cook")
@Entity
public class Cook {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="available", columnDefinition="boolean default true")
	private boolean available;
	
	@Column(name="NTA", nullable = true, columnDefinition = "varchar(5) default '00:00'") // Next time available
	private String NTA;
	
	@Column(name="time_busy")
	private int timeBusy;
	
	//List of recipes to be cooked<OrderID,recipeName >
	@Column(name="recipes_to_cook", nullable = true, columnDefinition = "LONGTEXT")
	private HashMap<String,String> recipesToCook;
	
}
