package com.nicolas.component.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by nico on 23/06/16.
 */
@Entity
@Table(name="customer")
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Customer {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement(name = "id")
    private int id;

    @Column(name = "ref")
    @XmlElement(name = "ref")
    private String ref;

    @Column(name = "name", nullable = false)
    @XmlElement(name = "name")
    private String name;

    @Column(name = "address", nullable = false)
    @XmlElement(name = "address")
    private String address;

    @Column(name = "postal_code", nullable = false)
    @XmlElement(name = "postal_code")
    private String postalCode;

    @Column(name = "city", nullable = false)
    @XmlElement(name = "city")
    private String city;

    @Column(name = "phone")
    @XmlElement(name = "phone")
    private String phone;

    @Column(name = "email", nullable = false)
    @XmlElement(name = "email")
    private String email;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Order> orders;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @PostPersist void onPostPersist() {
        NumberFormat formatter = new DecimalFormat("00000000");
        this.setRef("CUS" + formatter.format(this.id));
    }

    @Override
    public String toString() {
        return this.name;
    }
}