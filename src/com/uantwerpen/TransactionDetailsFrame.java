package com.uantwerpen;

import com.uantwerpen.Helpers.DbWriter;
import com.uantwerpen.Objects.Transaction;

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

    private DbWriter dbWriter = new DbWriter();

    private Transaction transaction;

    public TransactionDetailsFrame(Integer transactionId) {
        transaction = dbWriter.GetTransactionById(transactionId);
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
        labelTransactionPayee.setText("Payee: " + dbWriter.GetGroupMemberByMemberId(transaction.getPayeeId()).name);
        labelTransactionPayers.setText("Payer(s) ID(s): " + transaction.getJoinedPayerIds());
        labelTransactionDate.setText("Transaction date: " + transaction.getDateTime());
    }
}
