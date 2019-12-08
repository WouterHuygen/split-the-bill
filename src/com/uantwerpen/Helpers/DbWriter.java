package com.uantwerpen.Helpers;

import com.uantwerpen.Objects.GroupMember;

import com.uantwerpen.Objects.PaymentGroup;
import com.uantwerpen.Objects.Transaction;

import java.sql.*;
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
        String sqlQuery= "SELECT memberId, name, groupid, balance, email FROM GROUPMEMBERS WHERE groupid == ?";
        String result = "";
        ArrayList<GroupMember> groupMembers = new ArrayList<>();
        try (Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(sqlQuery)){

            pstmt.setInt(1, groupId);

            ResultSet rs  = pstmt.executeQuery();
            while (rs.next()){
                GroupMember groupMember = new GroupMember(rs.getString("name"), rs.getString("email"),rs.getInt("groupId"), 0);
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

    //Get groupname based on ID
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

    //Get groupmember based on ID
    public GroupMember GetGroupMemberByMemberId(int memberId){
        String sqlQuery = "SELECT * FROM GROUPMEMBERS WHERE memberid == ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sqlQuery)){

            pstmt.setDouble(1,memberId);

            ResultSet rs  = pstmt.executeQuery();
            GroupMember groupMember = new GroupMember(rs.getString("name"), rs.getString("email"), rs.getInt("groupId"), rs.getInt("balance"));
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
            GroupMember groupMember = new GroupMember(rs.getString("name"), rs.getString("email"), rs.getInt("groupId"), rs.getInt("balance"));
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

    /*public void AddTransaction(Transaction transaction){
        String sqlQuery= "INSERT INTO TRANSACTIONS(paymentgoupid, name, description, payeeid, payerids) VALUES(?, ?, ?, ?, ?)";

        try(Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setInt(1, transaction.getPaymentGroupId());
            pstmt.setString(2, transaction.getName());
            pstmt.setDouble(3, transaction.getAmount());
            pstmt.setString(4, transaction.getDescription());
            pstmt.setInt(5, transaction.getPayeeId());
            pstmt.setString(6, transaction.getJoinedPayerIds());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }*/

    // Initializes and opens the database
    private void InitializeDatabase(){
        InitializePaymentGroupsTable();
        InitializeGroupMembersTable();
        InitializeTransactionsTable();
    }
    private void InitializePaymentGroupsTable(){
        String sqlQuery = "CREATE TABLE if NOT EXISTS PAYMENTGROUPS"+
                "(GROUPID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
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
                "(MEMBERID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME CHAR(50)    NOT NULL," +
                " EMAIL CHAR(50)    NOT NULL," +
                " GROUPID INTEGER    NOT NULL," +
               // " FOREIGN KEY GROUPID REFERENCES PAYMENTGROUPS(GROUPID), " +
                " BALANCE INTEGER NOT NULL);";
        try(Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private void InitializeTransactionsTable(){
        String sqlQuery = "CREATE TABLE if NOT EXISTS TRANSACTIONS"+
                "(TRANSACTIONID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                " PAYMENTGROUPID    CHAR(50)        NOT NULL, " +
               // " FOREIGN KEY PAYMENTGROUPID REFERENCES PAYMENTGROUPS(GROUPID), " +
                " NAME              CHAR(50)        NOT NULL, " +
                " AMOUNT            INTEGER         NOT NULL, " +
                " DESCRIPTION       NVARCHAR(100), " +
                " PAYEEID           INTEGER         NOT NULL, " +
                //" PAYEEID INTEGER FOREIGN KEY REFERENCES GROUPMEMBERS(MEMBERID), " +
                " PAYERIDS          NVARCHAR(100)   NOT NULL," +
                " DATETIME          TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        try(Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}

