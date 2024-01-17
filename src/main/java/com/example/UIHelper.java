package com.example;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class UIHelper {
    static int distFromActLbl = 10;

    public JLabel createActionLbl(String text, int num) {
        // Number means action label number starting from 0
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        // Increase x cord for every element
        label.setBounds(60+(100 * num), 150, 80, 50);
        label.setOpaque(true);
        label.setBackground(Color.gray);

        return label;
    }

    public JLabel createRegisterLabel(int num) {
        // Number means register number starting from 0
        JLabel label = new JLabel("Register " + num, SwingConstants.CENTER);
        label.setBounds(60, 200 + (num * 30) + distFromActLbl, 100, 30);

        return label;
    }

    public JLabel createRegister(int num) {
        // Number means register number starting from 0
        JLabel label = new JLabel("000", SwingConstants.CENTER);
        label.setBounds(160, 200 + (num * 30) + distFromActLbl, 80, 50);
        label.setOpaque(true);
        label.setBackground(Color.lightGray);

        return label;
    }

    public JLabel createDecodeStage(int num) {
        // Number means decode stage number starting from 0
        JLabel label = new JLabel("");
        label.setBounds(200, 470 + (num * 30), 150, 28);
        label.setOpaque(true);
        label.setBackground(Color.lightGray);

        return label;
    }
}
