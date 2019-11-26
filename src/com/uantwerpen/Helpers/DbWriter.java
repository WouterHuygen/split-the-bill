package com.uantwerpen.Helpers;

import com.uantwerpen.MainApplication;
import com.uantwerpen.Objects.PaymentGroup;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;

public class DbWriter {
    public static String sqlUrl = "jdbc:sqlite:C:\\sqlite\\db\\splitit.db";

    private Connection connect(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(sqlUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    //Add new member to a paymentgroup
    public void InsertMember(String name, String email, String groupId){
        String sqlQuery= "INSERT INTO GroupMembers(name, email, groupid, saldo) VALUES(?, ?, ?, 0)";

        try(Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, groupId);
            //pstmt.setInt(4, 0);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Add new group to db
    public void InsertGroup(String name){
        String sqlQuery= "INSERT INTO PaymentGroups(name) VALUES(?)";

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
        String sqlQuery= "SELECT groupId, name FROM paymentgroups";
        String result = "";
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery)){
                while (rs.next()){
                    result += rs.getString("name") + "\n";
                    System.out.println(rs.getInt("groupId") + "\t" + rs.getString("name"));
                }
                return result;
        }catch(SQLException e){
                System.out.println(e.getMessage());
                return null;
        }
    }

    public ArrayList<PaymentGroup> GetAllPaymentGroupsB() {
        ArrayList<PaymentGroup> paymentgroupsList = new ArrayList<>();

        String sqlQuery = "SELECT * FROM paymentgroups";

        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery)){

            PaymentGroup paymentGroup;
            while (rs.next()){
                paymentGroup = new PaymentGroup(rs.getInt("groupId"), rs.getString("name"), false);
                paymentgroupsList.add(paymentGroup);
                System.out.println(rs.getInt("groupId") + "\t" + rs.getString("name"));
            }
            return paymentgroupsList;
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    //Get groupname based on ID
    public String GetPaymentGroupById(int groupId){
        String sqlQuery = "SELECT groupId, name FROM paymentgroups WHERE groupId == ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sqlQuery)){

            pstmt.setDouble(1,groupId);

            ResultSet rs  = pstmt.executeQuery();
            System.out.println("PaymentGroup = " + rs.getString("name"));
/*            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("name") + "\t" +
                        rs.getDouble("capacity"));
            }*/
            return rs.getString("name");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //Get groupID based on name
    public int GetPaymentGroupById(String groupName){
        String sqlQuery = "SELECT groupId FROM paymentgroups WHERE name == ?";

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
}

