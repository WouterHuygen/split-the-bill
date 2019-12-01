package com.uantwerpen;

import com.uantwerpen.Helpers.DbWriter;
import com.uantwerpen.Objects.PaymentGroup;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainApplication extends JFrame{
    private JButton makeGrpBtn;
    private JButton gotoTransactionBtn;
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JTable paymentgroupsTbl;
    private JTextArea paymentGroupTa;

    private JPanel panel1 = new JPanel();
    private JPanel panel2 = new JPanel();

    public static void main(String[] args) {

        /*JFrame mainFrame = new JFrame("MainApplication");
        mainFrame.setContentPane(new MainApplication().mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);*/
        MainApplication frame = new MainApplication();
        frame.setBounds(200, 200, 700, 500);
        frame.setVisible(true);

        //new MainApplication().DisplayGroups();

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
            row[1]=list.get(i).getPaymentGroupName();
            model.addRow(row);
        }

        paymentgroupsTbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        paymentgroupsTbl.getColumnModel().getColumn(0).setMaxWidth(100);
    }

    private class ChangePanelAction implements ActionListener {

        private JPanel panel;
        private ChangePanelAction(JPanel pnl) {
            this.panel = pnl;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            changePanel(panel);

        }
    }

    private void initMenu() {
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem menuItem1 = new JMenuItem("Panel1");
        JMenuItem menuItem2 = new JMenuItem("Panel2");
        menubar.add(menu);
        menu.add(menuItem1);
        menu.add(menuItem2);
        setJMenuBar(menubar);
        TransactionPanel tp = new TransactionPanel();
        menuItem1.addActionListener(new ChangePanelAction(mainPanel));
        menuItem2.addActionListener(new ChangePanelAction(panel2));

    }

    private void changePanel(JPanel panel) {
        getContentPane().removeAll();
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().doLayout();
    }


    public MainApplication() {
        /*makeGrpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DisplayGroups();
            }
        });

        gotoTransactionBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                //GroupPanel gp = new GroupPanel();
                //gp.NewScreen();
                //mainFrame.setContentPane(new TransactionPanel().transactionPanel);
            }
        });*/

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initMenu();
        panel1.setBackground(Color.BLUE);
        panel2.setBackground(Color.RED);
        setLayout(new BorderLayout());
    }
}
