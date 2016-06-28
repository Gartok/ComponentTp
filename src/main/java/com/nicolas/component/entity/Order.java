package com.nicolas.component.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nicolas.component.CustomJsonDateDeserializer;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by nico on 23/06/16.
 */
@Entity
@Table(name="component_order")
@XmlRootElement()
@XmlAccessorType(XmlAccessType.NONE)
public class Order {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement(name = "id")
    private int id;

    @Column(name="ref")
    @XmlElement(name = "ref")
    private String ref;

    @Column(name="date_created")
    @XmlElement(name = "dateCreated")
    @JsonFormat(pattern="yyyy-MM-dd'T'hh:mm:ss.SSSZ")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss.SSSZ")
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    private Date dateCreated;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @XmlAnyElement
    private List<OrderDetail> orderDetails;

    @ManyToOne
    @XmlElement(name = "customer")
    private Customer customer;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @XmlAnyElement
    private List<Delivery> deliveries;

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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @PostPersist void onPostPersist() {
        NumberFormat formatter = new DecimalFormat("00000000");
        this.setRef("ORD" + formatter.format(this.id));
    }

    @Override
    public String toString() {
        return this.getRef();
    }
}