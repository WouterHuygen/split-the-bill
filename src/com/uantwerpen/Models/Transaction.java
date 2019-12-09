package com.uantwerpen.Models;

public class Transaction {
    private Integer id;
    private Integer paymentGroupId;
    private String name;
    private Double amount;
    private String description;
    private Integer payeeId;
    private Integer[] payerIds;
    private String joinedPayerIds;
    private java.util.Date dateTime;

    public Transaction(Integer paymentGroupId, String transactionName, Double amount, Integer payeeId, Integer[] payerIds) {
        this.paymentGroupId = paymentGroupId;
        this.name = transactionName;
        this.amount = amount;
        this.payeeId = payeeId;
        this.payerIds = payerIds;
        this.joinedPayerIds = joinPayerIdsAsString(payerIds);
    }

    public Transaction(Integer paymentGroupId, String transactionName, Double amount, String transactionDescription, Integer payeeId, Integer[] payerIds) {
        this.paymentGroupId = paymentGroupId;
        this.name = transactionName;
        this.amount = amount;
        this.description = transactionDescription;
        this.payeeId = payeeId;
        this.payerIds = payerIds;
        this.joinedPayerIds = joinPayerIdsAsString(payerIds);
    }

    public Transaction(Integer paymentGroupId, String transactionName, Double amount, String transactionDescription, Integer payeeId, Integer[] payerIds, Integer id, java.util.Date dateTime) {
        this.paymentGroupId = paymentGroupId;
        this.name = transactionName;
        this.amount = amount;
        this.description = transactionDescription;
        this.payeeId = payeeId;
        this.payerIds = payerIds;
        this.joinedPayerIds = joinPayerIdsAsString(payerIds);
        this.id = id;
        this.dateTime = dateTime;
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

    public String getJoinedPayerIds() { return joinedPayerIds; }

    public java.util.Date getDateTime() {
        return dateTime;
    }


    private String joinPayerIdsAsString(Integer[] payerIds) {
        String joinedPayerIds = "";
        for (Integer payerId: payerIds) {
            joinedPayerIds += (payerId.toString() + " ");
        }
        return joinedPayerIds;
    }
}
