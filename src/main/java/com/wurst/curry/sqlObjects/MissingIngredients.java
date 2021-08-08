package com.wurst.curry.sqlObjects;


import org.springframework.stereotype.Component;

import lombok.Data;


//Class used to map the queries declared in nativeSQL used to find the ingredients 
//missing for the order

@Data
@Component
public class MissingIngredients {
	String name;
	int storageUnits;
	int unitsNeeded;
	int deliveryTime;
	int unitsMissing;
	
	public MissingIngredients() {
       
    }

	public MissingIngredients(Object[] columns) {
        this.name = (String) columns[0] ;
        this.storageUnits = (int) columns[1];
        this.unitsNeeded = (int) columns[2];
        this.deliveryTime = (int) columns[3];
        this.unitsMissing = (int) columns[4];
    }

}
