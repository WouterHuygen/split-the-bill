package com.uantwerpen.Objects;

public class GroupMember {
    public String Name;
    public String Email;
    public String Group;
    public int GroupId;
    public int Saldo;

    public GroupMember(String name, String email, int groupId, int saldo) {
        Name = name;
        Email = email;
        GroupId = groupId;
    }

    public int getGroupId() {
        return GroupId;
    }

    public void setGroupId(int groupId) {
        GroupId = groupId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String Group) {
        this.Group = Group;
    }

    public int getSaldo() {
        return Saldo;
    }

    public void setSaldo(int Saldo) {
        this.Saldo = Saldo;
    }
}
