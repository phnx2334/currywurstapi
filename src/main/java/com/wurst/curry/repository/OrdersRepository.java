package com.wurst.curry.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wurst.curry.model.Orders;

public interface OrdersRepository extends JpaRepository<Orders, String> {

	//Find all orders and list from newest to oldest
	List<Orders> findAllByOrderByIdDesc();
	
	//Find all orders and list from newest to oldest
	List<Orders> findAllByOrderByIdAsc();
}

