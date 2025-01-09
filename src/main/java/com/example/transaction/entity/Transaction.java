package com.example.transaction.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OptimisticLocking;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "transactions")
@OptimisticLocking
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "trx_amount", nullable = false)
    private Double trxAmount;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "trx_date", nullable = false)
    private java.time.LocalDate trxDate;

    @Column(name = "trx_time", nullable = false)
    private java.time.LocalTime trxTime;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Version
    private Integer version;  // For Optimistic Locking

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getTrxAmount() {
        return trxAmount;
    }

    public void setTrxAmount(Double trxAmount) {
        this.trxAmount = trxAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTrxDate() {
        return trxDate;
    }

    public void setTrxDate(LocalDate trxDate) {
        this.trxDate = trxDate;
    }

    public LocalTime getTrxTime() {
        return trxTime;
    }

    public void setTrxTime(LocalTime trxTime) {
        this.trxTime = trxTime;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
