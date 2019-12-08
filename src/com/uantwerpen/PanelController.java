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

    public Integer getCurrentGroupId() { return currentGroupId; }
    private Integer currentGroupId;

    public CardLayout cl = new CardLayout();


    private PanelController(){
        panelAlt.setLayout(cl);
        panelAlt.setPreferredSize( new Dimension( 900, 600 ) );

        panelAlt.add(new MenuPanel().getMenuPanel(), "1");

        /** You can navigate to a specify panel using the cl.show() method as shown below
         * Always specify 'panelMain' as the first argument because we added all panels we want to be able to navigate to above
         * Also specify the panel constraints **/
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

    public void makeGroupPanel(){
        panelAlt.add(new GroupPanel().ManagePaymentGroup(currentGroupId), "GroupPanel");
        cl.show(panelAlt, "GroupPanel");
    }

    public void makeCreateGroupPanel(){
        panelAlt.add(new GroupPanel().getCreateGroupPanel(), "CreateGroupPanel");
        cl.show(panelAlt, "CreateGroupPanel");
    }

    public void makeMainPanel(){
        panelAlt.add(new MenuPanel().getMenuPanel(), "MainPanel");
        cl.show(panelAlt, "MainPanel");
    }

    public void makeGroupOverviewPanel(int groupId){
        currentGroupId = groupId;
        panelAlt.add(new GroupOverviewPanel().getGroupOverviewPanel(), "GroupOverviewPanel");
        cl.show(panelAlt, "GroupOverviewPanel");
    }

    public static PanelController getInstance(){
        if (uniqueInstance == null){
            uniqueInstance = new PanelController();
        }
        return uniqueInstance;
    }
}
