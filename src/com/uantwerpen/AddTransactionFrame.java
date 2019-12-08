package com.uantwerpen;

import com.uantwerpen.Helpers.DbWriter;
import com.uantwerpen.Objects.GroupMember;
import com.uantwerpen.Objects.Transaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
    private JPanel panelMiddle;
    private JPanel panelBottom;
    private JSpinner spinnerAmount;
    private JButton buttonAddTransaction;
    private JPanel panelMiddle1;
    private JPanel panelCheckBoxes;

    private DbWriter dbWriter = new DbWriter();

    private ArrayList<GroupMember> groupMembers = new ArrayList<>();

    public TransactionFrame() {

        initTransactionFrame();

        groupMembers = dbWriter.GetMembersByGroupId(1); // ID moet nog dynamisch worden voor de huidige geselcteerde groep

        initComboBox();
        initCheckBoxes();

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
        BoxLayout bl = new BoxLayout(panelCheckBoxes, BoxLayout.Y_AXIS);
        panelCheckBoxes.setLayout(bl);
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
        Integer payeeId = getIdForName((String)comboBoxPayer.getSelectedItem());
        ArrayList<Integer> selectedPayerIds = getCheckedPayerIds();
        Integer[] payerIds = selectedPayerIds.toArray(new Integer[selectedPayerIds.size()]);

        Transaction testTransaction = new Transaction(paymentGroupId, paymentName, amount, paymentDescription, payeeId, payerIds);
        dbWriter.InsertTransaction(testTransaction);
    }

    private void initComboBox(){
        for (GroupMember member: groupMembers) {
            comboBoxPayer.addItem(member.name);
        }
    }

    private void initCheckBoxes(){
        for (GroupMember member: groupMembers) {
            System.out.println(member.name);
            panelCheckBoxes.add(new JCheckBox(member.name));
        }
    }

    private Integer getIdForName(String name){
        Integer id = null;
        for (GroupMember member: groupMembers) {
            if (member.name == name){
                id = member.memberId;
            }
        }
        return id;
    }

    private ArrayList<Integer> getCheckedPayerIds(){
        ArrayList<Integer> payerIds = new ArrayList<>();
        Component[] components = panelCheckBoxes.getComponents();
        for (Component c: components) {
            if (c instanceof JCheckBox) {
                if (((JCheckBox) c).isSelected()){
                    payerIds.add(getIdForName(((JCheckBox) c).getText()));
                }
            }
        }
        return payerIds;
    }
}
