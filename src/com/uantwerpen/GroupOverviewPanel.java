package com.uantwerpen;

import com.uantwerpen.Helpers.DbWriter;
import com.uantwerpen.Objects.GroupMember;
import com.uantwerpen.Objects.Transaction;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;

public class GroupOverviewPanel {
    private JPanel groupOverviewPanel;
    private JButton buttonManageGroup;
    private JButton buttonAddTransaction;
    private JPanel panelTransactionHistory;
    private JPanel panelMemberBalances;

    public JPanel getGroupOverviewPanel() {
        return groupOverviewPanel;
    }

    private DbWriter dbWriter = new DbWriter();

    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    private ArrayList<String> transactionNames = new ArrayList<String>();

    public GroupOverviewPanel(){
        transactions = dbWriter.GetTransactionsByGroupId(PanelController.getInstance().getCurrentGroupId());

        for (Transaction t: transactions) {
            transactionNames.add(t.getName());
        }

        initTransactionList();
        initMemberBalances();

        buttonAddTransaction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                AddTransactionFrame transactionFrame = new AddTransactionFrame();
            }
        });
        buttonManageGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PanelController.getInstance().makeGroupPanel();
            }
        });
    }

    private void initTransactionList(){
        JLabel l = new JLabel("Select a transaction");

        JList listTransactions = new JList(transactionNames.toArray());

        listTransactions.addListSelectionListener(listSelectionListener);

        panelTransactionHistory.add(listTransactions);
    }
    
    private void initMemberBalances(){
        panelMemberBalances.setLayout(new BoxLayout(panelMemberBalances, BoxLayout.PAGE_AXIS));
        ArrayList<GroupMember> groupMembers = dbWriter.GetMembersByGroupId(PanelController.getInstance().getCurrentGroupId());
        for (GroupMember m: groupMembers) {
            JLabel label = new JLabel("Balance of " + m.getName() + ": € " + Math.round(m.getBalance()*100.0)/100.0);
            panelMemberBalances.add(label);
        }
    }

    private Integer getIdForTransactionName(String name){
        Integer id = null;
        for (Transaction transaction: transactions) {
            if (transaction.getName() == name){
                id = transaction.getId();
            }
        }
        return id;
    }

    ListSelectionListener listSelectionListener = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            boolean adjust = listSelectionEvent.getValueIsAdjusting();
            if (!adjust) {
                JList list = (JList) listSelectionEvent.getSource();
                Object selectionValue = list.getSelectedValue();
                    String selectedTransactionNameString = (String) selectionValue;
                    TransactionDetailsFrame detailsFrame = new TransactionDetailsFrame(getIdForTransactionName(selectedTransactionNameString));
                }
            }
    };


}
