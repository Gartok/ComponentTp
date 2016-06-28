package com.nicolas.component.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

/**
 * Created by nico on 23/06/16.
 */
@Entity
@Table(name="delivery")
public class Delivery {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="ref", nullable = false)
    private String ref;

    @Column(name="delivery_date", nullable = false)
    private Date deliveryDate;

    @ManyToOne
    @JsonIgnore
    private Order order;

    @ManyToOne
    private Invoice invoice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @PostPersist void onPostPersist() {
        NumberFormat formatter = new DecimalFormat("00000000");
        this.setRef("DEL" + formatter.format(this.id));
    }

    @Override
    public String toString() {
        return this.getRef();
    }
}