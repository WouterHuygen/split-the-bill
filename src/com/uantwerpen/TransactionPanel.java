package com.uantwerpen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TransactionPanel {

    public JPanel getTransactionPanel() { return transactionPanel; }
    private JPanel transactionPanel;
    private JButton buttonGroups;

    public TransactionPanel() {
        buttonGroups.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PanelController.getInstance().cl.show(PanelController.getInstance().getPanelAlt(), "2");
            }
        });
    }
}
