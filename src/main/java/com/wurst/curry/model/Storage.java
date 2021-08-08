package com.wurst.curry.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;



@Data
@Entity
@Table(name="storage")
public class Storage {
	
	@JsonIgnore
	@OneToMany(mappedBy = "ingredient")
	Set<IngredientsStorage> recipeIngredients;
	
	@Id
	@Column(name="food_item_name")
	private String name;
	
	@Column(name="available_units")
	private int availableUnits;
	

}
