package com.uantwerpen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** This class is a singleton **/
public class PanelController {

    private static PanelController uniqueInstance;

    private JFrame frameMain = new JFrame("Split The Bill");
    private JPanel panelMain;
    private JLabel titleLabel;
    private JButton buttonMainMenu;

    public JPanel getPanelAlt() { return panelAlt; }
    private JPanel panelAlt;

    public CardLayout cl = new CardLayout();


    private PanelController(){
        panelAlt.setLayout(cl);
        panelAlt.setPreferredSize( new Dimension( 900, 600 ) );

        /** This section is where you add new panels
         * Each panel gets a panel ID which you manually add
         * Only add panels that will be navigated to in the same frame **/
        panelAlt.add(new MenuPanel().getMenuPanel(), "1");
        panelAlt.add(new GroupPanel().getCreateGroupPanel(), "2");
        panelAlt.add(new GroupPanel().OpenPaymentGroup(1), "3");
        panelAlt.add(new GroupOverviewPanel().getGroupOverviewPanel(), "4");

        //panelAlt.add(new TransactionPanel().getTransactionPanel(), "3");

        /** You can navigate to a specify panel using the cl.show() method as shown below
         * Always specify 'panelMain' as the first argument because we added all panels we want to be able to navigate to above
         * Also specify the panel ID **/
        cl.show(panelAlt, "1");

        frameMain.add(panelMain);
        frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameMain.pack();
        frameMain.setLocationRelativeTo(null);
        frameMain.setVisible(true);

        buttonMainMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                makeMainPanel();
            }
        });
    }

    public void makeGroupPanel(int groupId){
        try{
            panelAlt.remove(2);
        }
        catch(Exception e){}

        panelAlt.add(new GroupPanel().OpenPaymentGroup(groupId), "3");
        cl.show(panelAlt, "3");
    }

    public void makeCreateGroupPanel(){
        panelAlt.add(new GroupPanel().getCreateGroupPanel(), "CreateGroupPanel");
        cl.show(panelAlt, "CreateGroupPanel");
    }

    public void makeMainPanel(){
        panelAlt.add(new MenuPanel().getMenuPanel(), "MainPanel");
        cl.show(panelAlt, "MainPanel");
    }

    public static PanelController getInstance(){
        if (uniqueInstance == null){
            uniqueInstance = new PanelController();
        }
        return uniqueInstance;
    }
}
