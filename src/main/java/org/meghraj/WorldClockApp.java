package org.meghraj;

import javax.swing.*;

public class WorldClockApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WorldClockAppGUI::new);
    }
}