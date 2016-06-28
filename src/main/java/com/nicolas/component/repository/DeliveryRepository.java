package com.nicolas.component.repository;


import com.nicolas.component.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {

}