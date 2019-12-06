package com.uantwerpen.Objects;

import java.sql.Timestamp;

public class Transaction {
    private Integer id;
    private Integer paymentGroupId;
    private String name;
    private Double amount;
    private String description;
    private Integer payeeId;
    private Integer[] payerIds;
    private Timestamp dateTime;

    public Transaction(Integer paymentGroupId, String transactionName, Integer payeeId, Integer[] payerIds) {
        this.paymentGroupId = paymentGroupId;
        this.name = transactionName;
        this.payeeId = payeeId;
        this.payerIds = payerIds;
    }

    public Transaction(Integer paymentGroupId, String transactionName, String transactionDescription, Integer payeeId, Integer[] payerIds) {
        this.paymentGroupId = paymentGroupId;
        this.name = transactionName;
        this.description = transactionDescription;
        this.payeeId = payeeId;
        this.payerIds = payerIds;
    }

    /** Getters **/
    public Integer getId() {
        return id;
    }

    public Integer getPaymentGroupId() {
        return paymentGroupId;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPayeeId() {
        return payeeId;
    }

    public Integer[] getPayerIds() {
        return payerIds;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public String getJoinedPayerIds() {
        String joinedPayerIds = "";
        for (Integer payerId: payerIds) {
            joinedPayerIds += (payerId.toString() + "; ");
        }
        return joinedPayerIds;
    }
}
