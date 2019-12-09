package com.uantwerpen.Controllers;

import com.uantwerpen.Models.GroupMember;

import java.sql.*;
import java.util.ArrayList;

public class GroupMemberController {
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

    public void DeleteAllMembersFromGroup(Integer groupId){
        String sqlQuery = "DELETE FROM GROUPMEMBERS WHERE GROUPID == ?";
        try(Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            pstmt.setInt(1, groupId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
