package com.nicolas.component.service;


import com.nicolas.component.repository.DeliveryRepository;
import com.nicolas.component.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

}