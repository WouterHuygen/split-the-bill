package com.uantwerpen.Helpers;

import com.uantwerpen.GroupPanel;
import com.uantwerpen.MainApplication;
import com.uantwerpen.Objects.GroupMember;
import com.uantwerpen.Objects.PaymentGroup;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;


public class DbWriter {
    public int newGroupId;

    public static String sqlUrl = "jdbc:sqlite:splitthebill.db";
    Connection conn = null;

    public DbWriter() {
        //InitializeDatabase();
    }

    private Connection connect(){
        try {
            conn = DriverManager.getConnection(sqlUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    //Add new member to a paymentgroup
    //public void InsertMember(String name, String email, int groupId){
    public void InsertMember(GroupMember memberToAdd){
        String sqlQuery= "INSERT INTO GROUPMEMBERS(name, email, groupid, saldo) VALUES(?, ?, ?, 0)";

        try(Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setString(1, memberToAdd.Name);
            pstmt.setString(2, memberToAdd.Email);
            pstmt.setInt(3, memberToAdd.GroupId);
            pstmt.executeUpdate();

            System.out.println("name = " + memberToAdd.Name + ", is added to group" + memberToAdd.GroupId);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    //Get members from a groupID
    public ArrayList<GroupMember> GetMembersByGroupId(int groupId){
        String sqlQuery= "SELECT memberId, name, groupid, saldo, email FROM GROUPMEMBERS WHERE groupid == ?";
        String result = "";
        ArrayList<GroupMember> groupMembers = new ArrayList<>();
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sqlQuery)){

            pstmt.setInt(1, groupId);


            ResultSet rs  = pstmt.executeQuery();
            while (rs.next()){
                GroupMember groupMember = new GroupMember();
                groupMember.Name = rs.getString("name");
                groupMember.Saldo = rs.getInt("saldo");
                groupMember.GroupId = rs.getInt("groupId");
                groupMember.Email = rs.getString("email");

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
        String sqlQuery= "INSERT INTO PAYMENTGROUPS(name) VALUES(?)";

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
                    System.out.println(rs.getInt("groupId") + "\t" + rs.getString("groupname"));
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
                paymentGroup = new PaymentGroup(rs.getInt("groupId"), rs.getString("groupname"), false);
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
/*            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("name") + "\t" +
                        rs.getDouble("capacity"));
            }*/
            return rs.getString("groupname");
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
            System.out.println("PaymentGroupID = " + rs.getInt("groupId"));

            return rs.getInt("groupId");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    // Initializes and opens the database
    private void InitializeDatabase(){
        InitializePaymentGroupTable();
    }
    private void InitializePaymentGroupTable(){
        String sqlQuery = "CREATE TABLE if NOT EXISTS PAYMENTGROUPS"+
                "(GROUPID INT PRIMARY KEY     NOT NULL," +
                " GROUPNAME           CHAR(50)    NOT NULL," +
                " ISSETTLED            INTEGER     NOT NULL)";;
        try(Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

