package com.uantwerpen;

import com.uantwerpen.Helpers.DbWriter;
import com.uantwerpen.Objects.Transaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TransactionFrame{

    private JFrame frameTransaction;
    public JPanel getPanelTransaction() { return panelTransaction; }
    private JPanel panelTransaction;
    private JButton buttonCancelTransaction;
    private JPanel panelMiddle2;
    private JPanel panelTitle;
    private JLabel titleLabel;
    private JComboBox comboBoxPayer;
    private JTextField textFieldTransactionName;
    private JTextField textFieldDescription;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JCheckBox checkBox3;
    private JPanel panelMiddle;
    private JPanel panelBottom;
    private JSpinner spinnerAmount;
    private JButton buttonAddTransaction;
    private JPanel panelMiddle1;

    private DbWriter dbWriter = new DbWriter();

    public TransactionFrame() {

        initTransactionFrame();

        buttonCancelTransaction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                frameTransaction.setVisible(false);
                frameTransaction.dispose();
            }
        });
        buttonAddTransaction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                addTransaction();
                frameTransaction.setVisible(false);
                frameTransaction.dispose();
            }
        });
    }
    private void initTransactionFrame(){
        frameTransaction = new JFrame();
        panelTransaction.setPreferredSize( new Dimension( 500, 600 ) );
        frameTransaction.add(panelTransaction);
        frameTransaction.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameTransaction.pack();
        frameTransaction.setLocationRelativeTo(null);
        frameTransaction.setVisible(true);
    }

    private void addTransaction(){
        Integer paymentGroupId = 1; // WIP: Is still default value
        String paymentName = textFieldTransactionName.getText();
        Double amount = (double) (int) spinnerAmount.getValue();
        String paymentDescription = textFieldDescription.getText();
        Integer payeeId = 4; // WIP: Is still default value
        Integer[] payerIds = {1,2,3}; // WIP: Is still default value

        Transaction testTransaction = new Transaction(paymentGroupId, paymentName, amount, paymentDescription, payeeId, payerIds);
        dbWriter.InsertTransaction(testTransaction);
    }
}
