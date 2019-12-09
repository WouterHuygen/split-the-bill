package com.uantwerpen.Controllers;

import com.uantwerpen.Models.Transaction;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TransactionController {
    public static String sqlUrl = "jdbc:sqlite:splitthebill.db";
    Connection conn = null;

    private Connection connect(){
        try {
            conn = DriverManager.getConnection(sqlUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void InsertTransaction(Transaction transaction){
        String sqlQuery= "INSERT INTO TRANSACTIONS(PAYMENTGROUPID, NAME, AMOUNT, DESCRIPTION, PAYEEID, PAYERIDS) VALUES(?, ?, ?, ?, ?, ?)";

        try(Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setInt(1, transaction.getPaymentGroupId());
            pstmt.setString(2, transaction.getName());
            pstmt.setDouble(3, transaction.getAmount());
            pstmt.setString(4, transaction.getDescription());
            pstmt.setInt(5, transaction.getPayeeId());
            pstmt.setString(6, transaction.getJoinedPayerIds());
            pstmt.executeUpdate();
            System.out.println("Transaction Added");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /** Gets a transaction by it's ID **/
    public Transaction GetTransactionById(Integer transactionId){
        String sqlQuery = "SELECT * FROM TRANSACTIONS WHERE TRANSACTIONID == ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sqlQuery)){
            pstmt.setInt(1, transactionId);
            ResultSet rs  = pstmt.executeQuery();

            Transaction transaction = new Transaction(
                    rs.getInt("PAYMENTGROUPID"),
                    rs.getString("NAME"),
                    rs.getDouble("AMOUNT"),
                    rs.getString("DESCRIPTION"),
                    rs.getInt("PAYEEID"),
                    convertJoinedPayerIdsToIntegerArray(rs.getString("PAYERIDS")),
                    rs.getInt("TRANSACTIONID"),
                    convertStringToDate(rs.getString("DATETIME"))
            );

            return transaction;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /** Gets a list of transactions for a groupID **/
    public ArrayList<Transaction> GetTransactionsByGroupId(Integer groupId){
        String sqlQuery = "SELECT * FROM TRANSACTIONS WHERE PAYMENTGROUPID == ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sqlQuery)){
            pstmt.setInt(1, groupId);
            ResultSet rs  = pstmt.executeQuery();

            ArrayList<Transaction> transactions = new ArrayList<Transaction>();

            Transaction transaction;
            while (rs.next()){
                transaction = new Transaction(
                        rs.getInt("PAYMENTGROUPID"),
                        rs.getString("NAME"),
                        rs.getDouble("AMOUNT"),
                        rs.getString("DESCRIPTION"),
                        rs.getInt("PAYEEID"),
                        convertJoinedPayerIdsToIntegerArray(rs.getString("PAYERIDS")),
                        rs.getInt("TRANSACTIONID"),
                        convertStringToDate(rs.getString("DATETIME"))
                );
                transactions.add(transaction);
            }
            return transactions;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /** Casts date string to a java date **/
    private java.util.Date convertStringToDate(String dateToConvert){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = null;
        try {
            date = df.parse(dateToConvert);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /** Casts the joined payer ids string to an integer array **/
    private Integer[] convertJoinedPayerIdsToIntegerArray(String stringToConvert){
        String[] joinedPayerIds = stringToConvert.trim().split(" ");
        Integer[] payerIds = new Integer[joinedPayerIds.length];
        for (int i = 0; i < payerIds.length; i++) {
            payerIds[i] = Integer.parseInt(joinedPayerIds[i]);
        }
        return payerIds;
    }
}
