package com.uantwerpen.Objects;

public class PaymentGroup {
    //ArrayList<GroupMember> Members;
    private int paymentGroupId;
    private String paymentGroupName;
    boolean IsSettled;

    public int getPaymentGroupId() {
        return paymentGroupId;
    }

    public String getPaymentGroupName() {
        return paymentGroupName;
    }

    public boolean isSettled() {
        return IsSettled;
    }

    public void setSettled(boolean settled) {
        IsSettled = settled;
    }

    public PaymentGroup(int paymentGroupId, String paymentGroupName, boolean isSettled){
        this.IsSettled = isSettled;
        this.paymentGroupName = paymentGroupName;
        this.paymentGroupId = paymentGroupId;
    }
}
