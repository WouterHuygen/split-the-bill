package com.uantwerpen.Objects;

public class GroupMember {
    public String name;
    public String email;
    public String group;
    public Integer groupId;
    public Integer balance;
    public Integer memberId;

    public GroupMember(String name, String email, Integer groupId, Integer balance) {
        this.name = name;
        this.email = email;
        this.groupId = groupId;
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

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(int Saldo) {
        this.balance = Saldo;
    }

    public boolean compareTo(GroupMember oldMember, GroupMember newMember){
        if(oldMember.name == newMember.name & oldMember.email == newMember.email & oldMember.balance == newMember.balance)
            return true;
        else
            return false;
    }
}
