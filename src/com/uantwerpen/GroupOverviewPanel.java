package com.uantwerpen;

import com.uantwerpen.Controllers.DbWriter;
import com.uantwerpen.Controllers.GroupMemberController;
import com.uantwerpen.Controllers.PaymentGroupController;
import com.uantwerpen.Controllers.TransactionController;
import com.uantwerpen.Models.GroupMember;
import com.uantwerpen.Models.PaymentGroup;
import com.uantwerpen.Models.Transaction;

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
    private JButton buttonSettleGroup;
    private JButton buttonPaymentOverview;
    private JButton buttonDeletePaymentGroup;

    private String settleText;

    public JPanel getGroupOverviewPanel() {
        return groupOverviewPanel;
    }

    private TransactionController tc = new TransactionController();
    private PaymentGroupController pgc = new PaymentGroupController();
    private GroupMemberController gmc = new GroupMemberController();

    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    private ArrayList<String> transactionNames = new ArrayList<String>();

    public ArrayList<GroupMember> memberList = new ArrayList<GroupMember>();
    public ArrayList<GroupMember> negativeMembers = new ArrayList<GroupMember>();
    public ArrayList<GroupMember> positiveMembers = new ArrayList<GroupMember>();


    public GroupOverviewPanel(){
        transactions = tc.GetTransactionsByGroupId(PanelController.getInstance().getCurrentGroupId());
        PaymentGroup currentPaymentGroup = pgc.GetGroupByGroupId(PanelController.getInstance().getCurrentGroupId());

        buttonDeletePaymentGroup.setVisible(false);

        if (currentPaymentGroup.isSettled()){
            buttonAddTransaction.setVisible(false);
            buttonManageGroup.setVisible(false);
            buttonSettleGroup.setVisible(false);
            buttonDeletePaymentGroup.setVisible(true);
        }

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
        buttonSettleGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int groupId = PanelController.getInstance().getCurrentGroupId();
                pgc.SettleGroupByGroupId(groupId);
                getPaymentOverview();
                JOptionPane.showMessageDialog(null, "Group is being settled, the following balances are still open: " + "\n" + settleText);
                PanelController.getInstance().makeMainPanel();
            }
        });
        buttonPaymentOverview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                getPaymentOverview();
                JOptionPane.showMessageDialog(null, settleText);
            }
        });
        buttonDeletePaymentGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                pgc.DeletePaymentGroup(PanelController.getInstance().getCurrentGroupId());
                gmc.DeleteAllMembersFromGroup(PanelController.getInstance().getCurrentGroupId());
                tc.DeleteAllTransactionsFromGroup(PanelController.getInstance().getCurrentGroupId());
                PanelController.getInstance().makeMainPanel();
            }
        });
    }

    private void getPaymentOverview(){
        settleText = "";
        negativeMembers.clear();
        positiveMembers.clear();
        memberList.clear();
        
        memberList = gmc.GetMembersByGroupId(PanelController.getInstance().getCurrentGroupId());
        for (GroupMember gm :
                memberList) {
            if (gm.balance == 0.0) continue;
            else if (gm.balance > 0) positiveMembers.add(gm);
            else negativeMembers.add(gm);
        }
        //Collections.sort(positiveMembers, GroupMember::compareTo);
        //System.out.println(positiveMembers);
        for (int i = 0; i < negativeMembers.size(); i++) {
            for (int j = 0; j < positiveMembers.size(); j++) {
                GroupMember negativeMember = negativeMembers.get(i);
                GroupMember positiveMember = positiveMembers.get(j);
                if(positiveMember.balance == 0.0) continue;
                else if (Math.abs(negativeMember.balance) > positiveMember.balance){
                    negativeMember.balance += positiveMember.balance;
                    settleText += (negativeMembers.get(i).name + " owes " + positiveMember.name + " €" + (double)Math.round(positiveMember.balance*100.0)/100.0 + "\n");
                    positiveMember.balance = 0.0;

                }else if(Math.abs(negativeMember.balance) < positiveMember.balance){
                    settleText += (negativeMember.name + " owes " + positiveMember.name + " €" + (double)Math.round(Math.abs(negativeMember.balance)*100.0)/100.0 + "\n");
                    negativeMember.balance = 0.0;
                    positiveMember.balance += negativeMember.balance;
                }else if(Math.abs(negativeMember.balance) == positiveMember.balance){
                    settleText += (negativeMember.name + " owes " + positiveMember.name + " €" + (double)Math.round(Math.abs(negativeMember.balance)) + "\n");
                    negativeMember.balance = 0.0;
                    positiveMember.balance = 0.0;
                }
            }
        }
    }

    private void initTransactionList(){
        JLabel l = new JLabel("Select a transaction");

        JList listTransactions = new JList(transactionNames.toArray());

        listTransactions.addListSelectionListener(listSelectionListener);

        panelTransactionHistory.add(listTransactions);
    }
    
    private void initMemberBalances(){
        panelMemberBalances.setLayout(new BoxLayout(panelMemberBalances, BoxLayout.PAGE_AXIS));
        ArrayList<GroupMember> groupMembers = gmc.GetMembersByGroupId(PanelController.getInstance().getCurrentGroupId());
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
