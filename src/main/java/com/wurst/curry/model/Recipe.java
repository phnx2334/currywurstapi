package com.wurst.curry.model;


import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name="recipe")
public class Recipe {
	
	@JsonIgnore
	@OneToMany(mappedBy = "recipe")
	Set<IngredientsStorage> recipeIngredients;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="time_to_cook")
	private int ttc;
	
	@Column(name="nutrients")
	@ElementCollection(targetClass=String.class)
	private List<String> nutrients;
	
	@Column(name="pre_made")
	private boolean preMade;

}
