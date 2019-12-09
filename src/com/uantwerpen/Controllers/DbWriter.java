package com.uantwerpen.Controllers;

import com.uantwerpen.Models.GroupMember;

import com.uantwerpen.Models.PaymentGroup;
import com.uantwerpen.Models.Transaction;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class DbWriter {
    public static String sqlUrl = "jdbc:sqlite:splitthebill.db";
    Connection conn = null;

    public DbWriter() {
        InitializeDatabase();
    }

    private Connection connect(){
        try {
            conn = DriverManager.getConnection(sqlUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }








    /** Initializes and opens the database **/
    private void InitializeDatabase(){
        InitializePaymentGroupsTable();
        InitializeGroupMembersTable();
        InitializeTransactionsTable();
    }
    private void InitializePaymentGroupsTable(){
        String sqlQuery = "CREATE TABLE if NOT EXISTS PAYMENTGROUPS"+
                "(GROUPID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +                    // Primary key
                " GROUPNAME CHAR(50) NOT NULL," +
                " ISSETTLED INTEGER NOT NULL);";
        try(Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void InitializeGroupMembersTable(){
        String sqlQuery = "CREATE TABLE if NOT EXISTS GROUPMEMBERS"+
                "(MEMBERID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +                   // Primary key
                " NAME CHAR(50)    NOT NULL," +
                " EMAIL CHAR(50)    NOT NULL," +
                " GROUPID INTEGER    NOT NULL," +
               // " FOREIGN KEY GROUPID REFERENCES PAYMENTGROUPS(GROUPID), " +
                " BALANCE DOUBLE NOT NULL);";
        try(Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private void InitializeTransactionsTable(){
        String sqlQuery = "CREATE TABLE if NOT EXISTS TRANSACTIONS"+
                "(TRANSACTIONID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +             // Primary key
                " PAYMENTGROUPID INTEGER REFERENCES PAYMENTGROUPS(GROUPID) NOT NULL, " +    // Foreign key
                " NAME              CHAR(50)        NOT NULL, " +
                " AMOUNT            DOUBLE         NOT NULL, " +
                " DESCRIPTION       NVARCHAR(100), " +
                " PAYEEID INTEGER REFERENCES GROUPMEMBERS(MEMBERID) NOT NULL, " +           // Foreign key
                " PAYERIDS          NVARCHAR(100)   NOT NULL," +
                " DATETIME          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL)";
        try(Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



}

