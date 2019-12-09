package com.uantwerpen.Helpers;

import com.uantwerpen.Objects.GroupMember;

import com.uantwerpen.Objects.PaymentGroup;
import com.uantwerpen.Objects.Transaction;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


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

    /**Add new member to a paymentgroup**/
    public void InsertMember(GroupMember memberToAdd){
        String sqlQuery= "INSERT INTO GROUPMEMBERS(name, email, groupid, BALANCE) VALUES(?, ?, ?, 0)";

        try(Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setString(1, memberToAdd.name);
            pstmt.setString(2, memberToAdd.email);
            pstmt.setInt(3, memberToAdd.groupId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    //public void InsertMember(String name, String email, int groupId){
    public void UpdateGroupMembers(ArrayList<GroupMember> updateList){
        for (GroupMember toUpdateMember:
             updateList) {
            String sqlQuery= "UPDATE GROUPMEMBERS SET name = ? , "
                    + "email = ? "
                    + "WHERE memberid = ?";
            try(Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
                pstmt.setString(1, toUpdateMember.name);
                pstmt.setString(2, toUpdateMember.email);
                pstmt.setInt(3, toUpdateMember.memberId);

                pstmt.executeUpdate();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //Get members from a groupID
    public ArrayList<GroupMember> GetMembersByGroupId(int groupId){
        String sqlQuery= "SELECT MEMBERID, NAME, EMAIL, GROUPID, BALANCE FROM GROUPMEMBERS WHERE groupid == ?";
        String result = "";
        ArrayList<GroupMember> groupMembers = new ArrayList<>();
        try (Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sqlQuery)){

            pstmt.setInt(1, groupId);

            ResultSet rs  = pstmt.executeQuery();
            while (rs.next()){
                GroupMember groupMember = new GroupMember(rs.getString("name"), rs.getString("email"),rs.getInt("groupId"), rs.getDouble("balance"));
                groupMember.memberId = rs.getInt("memberId");
                groupMembers.add(groupMember);
            }
            return groupMembers;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //Add new group to db
    public void InsertGroup(String name){
        String sqlQuery= "INSERT INTO PAYMENTGROUPS(groupname, issettled) VALUES(?, 0)";

        try(Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void SettleGroupByGroupId(int currentGroupId){
        String sqlQuery = "UPDATE PAYMENTGROUPS SET issettled = 1 WHERE GROUPID = ?";

        try(Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery)){
            pstmt.setInt(1, currentGroupId);
            pstmt.executeUpdate();
        }catch ( SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //Returns all groups
    public String GetAllPaymentGroups(){
        String sqlQuery= "SELECT groupId, groupname FROM PAYMENTGROUPS";
        String result = "";
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery)){
                while (rs.next()){
                    result += rs.getString("groupname") + "\n";
                }
                return result;
        }catch(SQLException e){
                System.out.println(e.getMessage());
                return null;
        }
    }

    public ArrayList<PaymentGroup> GetAllPaymentGroupsB() {
        ArrayList<PaymentGroup> paymentgroupsList = new ArrayList<>();

        String sqlQuery = "SELECT * FROM PAYMENTGROUPS";

        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery)){

            PaymentGroup paymentGroup;
            while (rs.next()){
                paymentGroup = new PaymentGroup(rs.getInt("groupId"), rs.getString("groupname"), rs.getBoolean("issettled"));
                paymentgroupsList.add(paymentGroup);
            }
            return paymentgroupsList;
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**Get groupname based on ID **/
    public String GetPaymentGroupById(int groupId){
        String sqlQuery = "SELECT groupId, groupname FROM PAYMENTGROUPS WHERE groupId == ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sqlQuery)){

            pstmt.setDouble(1,groupId);

            ResultSet rs  = pstmt.executeQuery();
            System.out.println("PaymentGroup = " + rs.getString("groupname"));
            return rs.getString("groupname");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public PaymentGroup GetGroupByGroupId(int groupId){
        String sqlQuery = "SELECT * FROM PAYMENTGROUPS WHERE groupId == ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sqlQuery)){

            pstmt.setDouble(1,groupId);

            ResultSet rs  = pstmt.executeQuery();
            PaymentGroup paymentGroup = new PaymentGroup(rs.getInt("groupId"), rs.getString("groupname"), rs.getBoolean("issettled"));
            return paymentGroup;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //Get groupmember based on ID
    public GroupMember GetGroupMemberByMemberId(int memberId){
        String sqlQuery = "SELECT * FROM GROUPMEMBERS WHERE memberid == ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sqlQuery)){

            pstmt.setDouble(1,memberId);

            ResultSet rs  = pstmt.executeQuery();
            GroupMember groupMember = new GroupMember(rs.getString("name"), rs.getString("email"), rs.getInt("groupId"), rs.getDouble("balance"));
            groupMember.memberId = rs.getInt("memberId");
            return groupMember;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //Get groupmember based on groupID and member Email
    public GroupMember GetGroupMemberByGroupIdAndEmail(int groupId, String email){
        String sqlQuery = "SELECT * FROM GROUPMEMBERS WHERE GROUPID == groupId AND EMAIL == email";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sqlQuery)){

            //pstmt.setDouble(1,memberId);

            ResultSet rs  = pstmt.executeQuery();
            GroupMember groupMember = new GroupMember(rs.getString("name"), rs.getString("email"), rs.getInt("groupId"), rs.getDouble("balance"));
            groupMember.memberId = rs.getInt("memberId");
            return groupMember;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //Get groupID based on name
    public int GetPaymentGroupIdByName(String groupName){
        String sqlQuery = "SELECT groupId FROM PAYMENTGROUPS WHERE groupname == ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sqlQuery)){

            pstmt.setString(1, groupName);

            ResultSet rs  = pstmt.executeQuery();

            return rs.getInt("groupId");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public void DeletePaymentGroup(int groupId){
        String sqlQuery = "DELETE FROM PAYMENTGROUPS WHERE groupid == ?";
        try(Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setInt(1, groupId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void DeleteMember(int memberId){
        String sqlQuery = "DELETE FROM GROUPMEMBERS WHERE memberid == ?";
        try(Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setInt(1, memberId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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

    public Double GetBalanceByMemberId(Integer memberId){
        String sqlQuery = "SELECT BALANCE FROM GROUPMEMBERS WHERE MEMBERID == ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sqlQuery)){
            pstmt.setInt(1, memberId);
            ResultSet rs  = pstmt.executeQuery();
            return rs.getDouble("BALANCE");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void UpdateMemberBalanceById(Double amountToAdd, Integer memberId){
            String sqlQuery= "UPDATE GROUPMEMBERS SET BALANCE = ? WHERE MEMBERID = ?";
            try(Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
                pstmt.setDouble(1, GetBalanceByMemberId(memberId) + amountToAdd);
                pstmt.setInt(2, memberId);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
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

