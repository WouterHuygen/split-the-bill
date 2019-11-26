package com.uantwerpen.Objects;

import java.util.ArrayList;

public class PaymentGroup {
    //ArrayList<GroupMember> Members;
    private int PaymentGroupId;
    private String Name;
    boolean IsSettled;

    public int getPaymentGroupId() {
        return PaymentGroupId;
    }

    public String getName() {
        return Name;
    }

    public PaymentGroup(int paymentGroupId, String name, boolean isSettled){
        this.IsSettled = isSettled;
        this.Name = name;
        this.PaymentGroupId = paymentGroupId;
    }
}
