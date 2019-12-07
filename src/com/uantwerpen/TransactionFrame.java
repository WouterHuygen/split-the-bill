package com.uantwerpen;

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
    private JTextField textField1;
    private JTextField textField2;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JCheckBox checkBox3;
    private JPanel panelMiddle;
    private JPanel panelBottom;
    private JSpinner spinner1;
    private JButton buttonAddTransaction;
    private JPanel panelMiddle1;

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
}
