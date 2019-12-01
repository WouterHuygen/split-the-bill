package com.uantwerpen;

import com.uantwerpen.Helpers.DbWriter;
import com.uantwerpen.Objects.PaymentGroup;
import javafx.scene.Group;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainApplication {
    private JButton addPaymentGroupBtn;
    private JPanel mainPanel;
    private JLabel titleLabel;
    public JTable paymentgroupsTbl;

    public JFrame mainFrame;

    public static void main(String[] args) {
        new MainApplication().MakeFrame();
    }
    public void MakeFrame(){
        MainApplication nw = new MainApplication();
        mainFrame = new JFrame("MainApplication");
        mainFrame.setContentPane(nw.mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);

        nw.DisplayGroups(nw.paymentgroupsTbl);
        nw.paymentgroupsTbl.setDefaultEditor(String.class, null);
    }

    public void DisplayGroups(JTable mainTable){
        DbWriter app = new DbWriter();

        ArrayList<PaymentGroup> list = app.GetAllPaymentGroupsB();
        DefaultTableModel model = (DefaultTableModel) mainTable.getModel();
        model.addColumn("GroupId");
        model.addColumn("GroupName");
        model.addColumn("IsSettled");
        Object[] headerRow = new Object[2];
        headerRow[0]="<html><b>GroupId</b></html>";
        headerRow[1]="<html><b>Paymentgroup name</b></html>";
        model.addRow(headerRow);

        Object[] row = new Object[3];
        for (int i=0; i<list.size(); i++){
            row[0]=list.get(i).getPaymentGroupId();
            row[1]=list.get(i).getPaymentGroupName();
            row[2]=list.get(i).isSettled();
            model.addRow(row);
        }

        TableColumnModel tcm = mainTable.getColumnModel();
        tcm.removeColumn(tcm.getColumn(2));

        mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        mainTable.getColumnModel().getColumn(0).setMaxWidth(100);
    }

    public MainApplication() {
        addPaymentGroupBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                GroupPanel gp = new GroupPanel();
                gp.NewScreen();
            }
        });

        paymentgroupsTbl.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if(!event.getValueIsAdjusting() && paymentgroupsTbl.getSelectedRow() != 0) {
                    try {
                        int groupId = (int) paymentgroupsTbl.getValueAt(paymentgroupsTbl.getSelectedRow(), 0);
                        boolean isSettled = (boolean) paymentgroupsTbl.getModel().getValueAt(paymentgroupsTbl.getSelectedRow(), 2);
                        System.out.println(isSettled);
                        if (!isSettled){
                            GroupPanel gp = new GroupPanel();
                            gp.OpenPaymentGroup(groupId);
                        }else {
                            JOptionPane.showMessageDialog(null, "This group has already been settled");
                        }
                    }catch (Exception e){
                        System.out.println("error = " + e);
                    }
                }
            }
        });
    }
}
