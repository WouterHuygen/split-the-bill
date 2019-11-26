package com.uantwerpen;

import com.uantwerpen.Helpers.DbWriter;
import com.uantwerpen.Objects.PaymentGroup;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainApplication {
    private JButton makeGrpBtn;
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JTable paymentgroupsTbl;
    private JTextArea paymentGroupTa;

    public static void main(String[] args) {

        JFrame mainFrame = new JFrame("MainApplication");
        mainFrame.setContentPane(new MainApplication().mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);

        new MainApplication().DisplayGroups();
    }

    public void DisplayGroups(){
        System.out.println("displaying groups");

        DbWriter app = new DbWriter();

        ArrayList<PaymentGroup> list = app.GetAllPaymentGroupsB();
        DefaultTableModel model = (DefaultTableModel) paymentgroupsTbl.getModel();
        model.addColumn("<html><b>GroupId</b></html>");
        model.addColumn("<html><b>Group Name</b></html>");
        Object[] headerRow = new Object[2];
        headerRow[0]="<html><b>GroupId</b></html>";
        headerRow[1]="<html><b>Paymentgroup name</b></html>";
        model.addRow(headerRow);

        Object[] row = new Object[2];
        for (int i=0; i<list.size(); i++){
            row[0]=list.get(i).getPaymentGroupId();
            row[1]=list.get(i).getName();
            model.addRow(row);
        }

        paymentgroupsTbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        paymentgroupsTbl.getColumnModel().getColumn(0).setMaxWidth(100);
    }

    public MainApplication() {
        makeGrpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                GroupPanel gp = new GroupPanel();
                gp.NewScreen();
            }
        });
    }
}
