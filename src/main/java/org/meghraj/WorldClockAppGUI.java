package org.meghraj;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class WorldClockAppGUI extends JFrame {

    private JComboBox<String>[] countryDropdowns = new JComboBox[4];
    private JLabel[] clockLabels = new JLabel[4];
    private JButton setAlarmButton;
    private JButton stopAlarmButton;
    private JLabel alarmStatusLabel;
    private Map<String, String> countryZoneIdMap; // Map to store country and its corresponding zone ID
    private Map<String, String> alarmDetails; // Map to store alarm details for each country
    private Clip alarmClip; // Sound clip for alarm

    public boolean alarmStopped = false;

    public WorldClockAppGUI() {
        setTitle("World Clock App");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initCountryZoneIdMap();
        initGUI();
        initClocks();
        initSound();

        setVisible(true);
    }

    private void initCountryZoneIdMap() {
        countryZoneIdMap = new HashMap<>();
        countryZoneIdMap.put("United Kingdom", "Europe/London");
        countryZoneIdMap.put("France", "Europe/Paris");
        countryZoneIdMap.put("Germany", "Europe/Berlin");
        countryZoneIdMap.put("Italy", "Europe/Rome");
        countryZoneIdMap.put("Spain", "Europe/Madrid");
        countryZoneIdMap.put("Netherlands", "Europe/Amsterdam");
        countryZoneIdMap.put("Belgium", "Europe/Brussels");
        countryZoneIdMap.put("Switzerland", "Europe/Zurich");
        countryZoneIdMap.put("Portugal", "Europe/Lisbon");
        countryZoneIdMap.put("Sweden", "Europe/Stockholm");
        countryZoneIdMap.put("Norway", "Europe/Oslo");
        countryZoneIdMap.put("Denmark", "Europe/Copenhagen");
        countryZoneIdMap.put("Finland", "Europe/Helsinki");
        countryZoneIdMap.put("Austria", "Europe/Vienna");
        countryZoneIdMap.put("Greece", "Europe/Athens");
        countryZoneIdMap.put("Poland", "Europe/Warsaw");
        countryZoneIdMap.put("Hungary", "Europe/Budapest");
        countryZoneIdMap.put("Czech Republic", "Europe/Prague");
        countryZoneIdMap.put("Romania", "Europe/Bucharest");
        countryZoneIdMap.put("Bulgaria", "Europe/Sofia");
        countryZoneIdMap.put("Slovakia", "Europe/Bratislava");
        countryZoneIdMap.put("Croatia", "Europe/Zagreb");
        countryZoneIdMap.put("Ireland", "Europe/Dublin");
        countryZoneIdMap.put("Lithuania", "Europe/Vilnius");
        countryZoneIdMap.put("Latvia", "Europe/Riga");
        countryZoneIdMap.put("Estonia", "Europe/Tallinn");
        countryZoneIdMap.put("Slovenia", "Europe/Ljubljana");
        countryZoneIdMap.put("Cyprus", "Europe/Nicosia");
        countryZoneIdMap.put("Luxembourg", "Europe/Luxembourg");
        countryZoneIdMap.put("Malta", "Europe/Malta");
        countryZoneIdMap.put("India", "Asia/Kolkata");
    }


    private void initGUI() {
        setLayout(new GridLayout(5, 2));

        // Add dropdowns for each clock and clock labels
        for (int i = 0; i < 4; i++) {
            JPanel clockPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            countryDropdowns[i] = new JComboBox<>(new String[]{"United Kingdom", "France", "Germany", "Italy",
                    "Spain", "Netherlands", "Belgium", "Switzerland", "Portugal", "Sweden", "Norway", "Denmark",
                    "Finland", "Austria", "Greece", "Poland", "Hungary", "Czech Republic", "Romania", "Bulgaria",
                    "Slovakia", "Croatia", "Ireland", "Lithuania", "Latvia", "Estonia", "Slovenia", "Cyprus", "Luxembourg",
                    "Malta", "India"});

            clockPanel.add(new JLabel("Select Country for Clock " + (i + 1) + ":"));
            clockPanel.add(countryDropdowns[i]);
            clockLabels[i] = new JLabel();
            clockPanel.add(clockLabels[i]);
            add(clockPanel);
        }

        // Add buttons for setting and stopping alarm
        JPanel alarmPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        setAlarmButton = new JButton("Set Alarm");
        setAlarmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement set alarm logic here
                Object selectedCountryObject = JOptionPane.showInputDialog(null, "Select country for alarm:",
                        "Set Alarm", JOptionPane.QUESTION_MESSAGE, null, countryZoneIdMap.keySet().toArray(), null);
                if (selectedCountryObject != null) {
                    String selectedCountry = selectedCountryObject.toString();
                    String selectedTime = (String) JOptionPane.showInputDialog(null, "Enter alarm time (HH:MM):",
                            "Set Alarm", JOptionPane.QUESTION_MESSAGE);
                    if (selectedTime != null && !selectedTime.isEmpty()) {
                        alarmDetails.put(selectedCountry, selectedTime);
                        alarmStatusLabel.setText("Alarm set for " + selectedTime + " in " + selectedCountry + ".");
                    }
                }
            }
        });
        alarmPanel.add(setAlarmButton);

        stopAlarmButton = new JButton("Stop Alarm");
        stopAlarmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopAlarmSound();
                alarmStatusLabel.setText("Alarm stopped.");
            }
        });
        alarmPanel.add(stopAlarmButton);

        alarmStatusLabel = new JLabel();
        alarmPanel.add(alarmStatusLabel);
        add(alarmPanel);

        alarmDetails = new HashMap<>();
    }





    private void initClocks() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Update clock labels with current time for each clock
                for (int i = 0; i < 4; i++) {
                    String selectedCountry = (String) countryDropdowns[i].getSelectedItem();
                    if (selectedCountry != null) {
                        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of(countryZoneIdMap.get(selectedCountry)));
                        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                        clockLabels[i].setText(formattedTime);

                        // Check if alarm time matches current time for the selected country
                        if (alarmDetails.containsKey(selectedCountry)) {
                            String[] alarmTime = alarmDetails.get(selectedCountry).split(":");
                            if (alarmTime.length == 2) {
                                int alarmHour = Integer.parseInt(alarmTime[0]);
                                int alarmMinute = Integer.parseInt(alarmTime[1]);
                                if (currentTime.getHour() == alarmHour && currentTime.getMinute() == alarmMinute) {
                                    playAlarmSound();
                                }
                            }
                        }
                    }
                }
            }
        }, 0, 1000);
    }

/*    private void initSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/main/resources/alarm.wav"));
            alarmClip = AudioSystem.getClip();
            alarmClip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }*/

    private void initSound() {
        try {
            // Load the audio file as a resource
            URL audioUrl = getClass().getResource("/alarm.wav");
            if (audioUrl != null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioUrl);
                alarmClip = AudioSystem.getClip();
                alarmClip.open(audioInputStream);
            } else {
                System.err.println("Error: Could not find alarm.wav in resources.");
            }
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }


    private void playAlarmSound() {
        if (alarmClip != null && !alarmClip.isRunning() && !alarmStopped) {
            alarmClip.setMicrosecondPosition(0);
            alarmClip.start();
            alarmStopped = true;
        }
    }

    private void stopAlarmSound() {
        if (alarmClip != null && alarmClip.isRunning()) {
            alarmClip.stop();
            alarmStopped = true; // so that alarm sound does not play in loop
            // alarmClip.setMicrosecondPosition(0); // Rewind the sound to the beginning
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WorldClockAppGUI();
            }
        });
    }
}
