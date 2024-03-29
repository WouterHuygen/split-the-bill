package com.uantwerpen.Models;

public class GroupMember {
    public String name;
    public String email;
    public String group;
    public Integer groupId;
    public Double balance;
    public Integer memberId;

    public GroupMember(String name, String email, Integer groupId, Double balance) {
        this.name = name;
        this.email = email;
        this.groupId = groupId;
        this.balance = balance;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String Email) {
        this.email = Email;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String Group) {
        this.group = Group;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double Balance) {
        this.balance = Balance;
    }

    public int compareTo(GroupMember b){
        return this.balance.compareTo(b.balance);
    }
}
