package com.nicolas.component.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by nico on 23/06/16.
 */
@Entity
@Table(name="invoice")
public class Invoice {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="ref")
    private String ref;

    @Column(name="invoice_date", nullable = false)
    private Date invoiceDate;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.EAGER)
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

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    @PostPersist void onPostPersist() {
        NumberFormat formatter = new DecimalFormat("00000000");
        this.setRef("INV" + formatter.format(this.id));
    }

    @Override
    public String toString() {
        return this.getRef();
    }
}