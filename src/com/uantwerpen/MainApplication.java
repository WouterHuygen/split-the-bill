package com.uantwerpen;

import javax.swing.*;

public class MainApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PanelController mainApp = PanelController.getInstance();
            }
        });

    }
}
