package com.wurst.curry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wurst.curry.model.CookingPlan;

public interface CookingPlanRepository extends JpaRepository<CookingPlan, String> {

}
