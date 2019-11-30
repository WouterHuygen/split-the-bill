package com.uantwerpen;

import com.uantwerpen.Helpers.DbWriter;
import com.uantwerpen.Objects.PaymentGroup;
import javafx.scene.Group;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainApplication {
    private JButton makeGrpBtn;
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JTable paymentgroupsTbl;

    public static void main(String[] args) {
        MainApplication nw = new MainApplication();

        JFrame mainFrame = new JFrame("MainApplication");
        mainFrame.setContentPane(nw.mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);

        new MainApplication().DisplayGroups(nw.paymentgroupsTbl);
    }

    public void DisplayGroups(JTable mainTable){
        DbWriter app = new DbWriter();

        ArrayList<PaymentGroup> list = app.GetAllPaymentGroupsB();
        DefaultTableModel model = (DefaultTableModel) mainTable.getModel();
        model.addColumn("GroupId");
        model.addColumn("GroupName");
        Object[] headerRow = new Object[2];
        headerRow[0]="<html><b>GroupId</b></html>";
        headerRow[1]="<html><b>Paymentgroup name</b></html>";
        model.addRow(headerRow);

        Object[] row = new Object[2];
        for (int i=0; i<list.size(); i++){
            row[0]=list.get(i).getPaymentGroupId();
            row[1]=list.get(i).getPaymentGroupName();
            model.addRow(row);
        }

        mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        mainTable.getColumnModel().getColumn(0).setMaxWidth(100);
        mainTable.setShowVerticalLines(false);
    }

    public MainApplication() {
        makeGrpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DbWriter dbWriter = new DbWriter();
            }
        });

        paymentgroupsTbl.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                DbWriter dbWriter = new DbWriter();

                if(!event.getValueIsAdjusting() && paymentgroupsTbl.getSelectedRow() != 0) {
                    try {
                        int groupId = (int) paymentgroupsTbl.getValueAt(paymentgroupsTbl.getSelectedRow(), 0);
                        GroupPanel gp = new GroupPanel();
                        gp.OpenPaymentGroup(groupId);

                    }catch (Exception e){
                        System.out.println("error = " + e);
                    }

                }
            }
        });
    }
}
