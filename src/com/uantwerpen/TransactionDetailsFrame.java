package com.uantwerpen;

import com.uantwerpen.Controllers.DbWriter;
import com.uantwerpen.Controllers.GroupMemberController;
import com.uantwerpen.Controllers.TransactionController;
import com.uantwerpen.Models.Transaction;

import javax.swing.*;
import java.awt.*;

public class TransactionDetailsFrame {

    private JFrame frameTransactionDetails;
    private JPanel panelTransactionDetails;
    private JLabel labelTransactionName;
    private JLabel labelTransactionAmount;
    private JLabel labelTransactionDescription;
    private JLabel labelTransactionPayee;
    private JLabel labelTransactionPayers;
    private JLabel labelTransactionDate;

    String payeeNamesString="";

    private TransactionController tc = new TransactionController();
    private GroupMemberController gmc = new GroupMemberController();

    private Transaction transaction;

    public TransactionDetailsFrame(Integer transactionId) {
        transaction = tc.GetTransactionById(transactionId);
        initTransactionFrame();
        initLabels();
    }

    private void initTransactionFrame(){
        frameTransactionDetails = new JFrame();
        panelTransactionDetails.setPreferredSize( new Dimension( 500, 600 ) );
        frameTransactionDetails.add(panelTransactionDetails);
        frameTransactionDetails.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameTransactionDetails.pack();
        frameTransactionDetails.setLocationRelativeTo(null);
        frameTransactionDetails.setVisible(true);
    }

    private void initLabels(){
        labelTransactionName.setText("Transaction name: " + transaction.getName());
        labelTransactionAmount.setText("Amount: € " + transaction.getAmount());
        labelTransactionDescription.setText("Description: " + transaction.getDescription());
        labelTransactionPayee.setText("Paid by: " + gmc.GetGroupMemberByMemberId(transaction.getPayeeId()).name);
        labelTransactionPayers.setText("Paid for: " + paidForNames(transaction.getPayerIds()));
        labelTransactionDate.setText("Transaction date: " + transaction.getDateTime());
    }

    private String paidForNames(Integer[] payeeIds){
        payeeNamesString ="";
        for (int memberId :
                payeeIds) {
            payeeNamesString += gmc.GetGroupMemberByMemberId(memberId).name +", ";
        }
        return payeeNamesString;
    }
}
