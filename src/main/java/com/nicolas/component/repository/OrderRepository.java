package com.nicolas.component.repository;


import com.nicolas.component.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findOneById(int id);
}