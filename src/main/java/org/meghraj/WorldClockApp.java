package org.meghraj;

import javax.swing.*;
import java.applet.Applet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class WorldClockApplet extends Applet {

    private JLabel clockLabel;
    private JComboBox<String> countryDropdown;
    private JButton setAlarmButton;
    private JButton stopAlarmButton;
    private JLabel alarmStatusLabel;

    @Override
    public void init() {
        clockLabel = new JLabel();
        countryDropdown = new JComboBox<>(new String[]{"Country 1", "Country 2", "Country 3"}); // Replace with actual country names
        setAlarmButton = new JButton("Set Alarm");
        stopAlarmButton = new JButton("Stop Alarm");
        alarmStatusLabel = new JLabel();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(clockLabel);
        add(countryDropdown);
        add(setAlarmButton);
        add(stopAlarmButton);
        add(alarmStatusLabel);

        // Fetch real-time time for selected country
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateClock();
            }
        }, 0, 1000);

        // Set alarm button action
        setAlarmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setAlarm();
            }
        });

        // Stop alarm button action
        stopAlarmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopAlarm();
            }
        });
    }

    private void updateClock() {
        // Fetch real-time time for selected country and update clockLabel
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = dateFormat.format(new Date());
        clockLabel.setText("Current Time: " + time);
    }

    private void setAlarm() {
        // Implement logic to set alarm for selected country and time, update alarmStatusLabel
        String selectedCountry = (String) countryDropdown.getSelectedItem();
        alarmStatusLabel.setText("Alarm set for " + selectedCountry);
    }

    private void stopAlarm() {
        // Implement logic to stop alarm, clear alarmStatusLabel
        alarmStatusLabel.setText("Alarm stopped");
    }
}
