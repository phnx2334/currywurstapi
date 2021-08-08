package com.wurst.curry.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="warehouse")
public class Warehouse {

	@Id
	@Column(name="name")
	private String name;
	
	@Column(name="delivery_time")
	private long deliveryTime;

}
