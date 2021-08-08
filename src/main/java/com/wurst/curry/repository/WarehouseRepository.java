package com.wurst.curry.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wurst.curry.model.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

	Optional<Warehouse> findWarehouseByName(String name);
}
