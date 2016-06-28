package com.nicolas.component.controller;

import com.nicolas.component.entity.EntityList;
import com.nicolas.component.entity.Order;
import com.nicolas.component.repository.OrderRepository;
import com.nicolas.component.repository.ProductRepository;
import com.nicolas.component.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by nico on 23/06/16.
 */
@RequestMapping("/api/order")
@RestController
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public EntityList<Order> getAll() {
        return new EntityList<>(this.orderRepository.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Order getById(@PathVariable int id) {
        return this.orderRepository.findOneById(id);
    }

    @RequestMapping(method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Order insert(@RequestBody Order order) {
        Order result = null;

        if (order != null) {
            result = this.orderRepository.save(order);
        }

        return result;
    }
}
