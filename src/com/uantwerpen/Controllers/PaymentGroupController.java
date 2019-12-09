package com.uantwerpen.Controllers;

import com.uantwerpen.Models.PaymentGroup;

import java.sql.*;
import java.util.ArrayList;

public class PaymentGroupController {
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

    //Settle a specific group
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
}
