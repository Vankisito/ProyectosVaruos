package com.NewCodeTeam.Comercializadora.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "concept")
    private  String concept;

    @Column(name = "amount")
    private float amount;


    @ManyToOne
    @JsonIgnoreProperties("user")
    @JoinColumn(name="id_user")
    private Employee user;
    @ManyToOne
    @JoinColumn(name="id_enterprise")
    private Enterprise enterprises;

    @Column(name="createdAt")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createdAt;

    @Column(name="updatedAt")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date updatedAt;

    public Transaction() {

    }

    public Transaction(String concept, float amount, Employee user, Enterprise enterprises, Date createdAt, Date updatedAt) {
        this.concept = concept;
        this.amount = amount;
        this.user = user;
        this.enterprises = enterprises;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Employee getUser() {
        return user;
    }

    public void setUser(Employee user) {
        this.user = user;
    }

    public Enterprise getEnterprises() {
        return enterprises;
    }

    public void setEnterprises(Enterprise enterprises) {
        this.enterprises = enterprises;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
