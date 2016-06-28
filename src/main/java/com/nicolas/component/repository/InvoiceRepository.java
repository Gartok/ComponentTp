package com.nicolas.component.repository;


import com.nicolas.component.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

}