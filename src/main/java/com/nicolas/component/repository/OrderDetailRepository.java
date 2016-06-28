package com.nicolas.component.repository;


import com.nicolas.component.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

}