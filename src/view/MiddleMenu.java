package view;

import java.awt.*;
import javax.swing.*;

import control.Controller;
import miscellaneous.Misc;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class MiddleMenu extends JPanel {
    private static final long serialVersionUID = 1L;

    // Components
    private ImageIcon logoIcon = new ImageIcon(getClass().getResource("../images/logo.png"));
    private JLabel logoLabel = new JLabel(logoIcon);
    private JLabel languageLabel = new JLabel();
    private JComboBox<String> languageBox = new JComboBox<>(Misc.LANGUAGE);
    private JButton designButton = new JButton("Design");
    private JButton randButton = new JButton("Random");
    private JLabel dimensionLabel = new JLabel();
    private JComboBox<String> dimensionBox = new JComboBox<>(Misc.DIMENSION);
    private JTextArea historyBox = new JTextArea(18, 18);
    private JScrollPane historyScrollPane = new JScrollPane(historyBox);
    private JLabel timerLabel = new JLabel();
    private JLabel timerBox = new JLabel();
    private int elapsedTime = 0;
    private Timer timer = new Timer(1000, e -> updateTimer());

    private JButton resetButton = new JButton("Reset");
    private JButton startButton = new JButton("Start");

    private Controller controller;
    private LinkedList<JPanel> panelList = new LinkedList<>();
    private LinkedList<JButton> buttonList = new LinkedList<>();

    /**
     * Creates a new MiddleMenu instance.
     * 
     * @param masterMind The controller responsible for handling user interactions.
     */
    public MiddleMenu(Controller masterMind) {
        controller = masterMind;
        setPreferredSize(new Dimension(240, 800));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(255, 254, 205));
        configurePanels();
        addComponentsToPanel();
        setLanguage();
        historyBox.setCaretPosition(0);
    }

    /**
     * Configures the panels within the MiddleMenu, arranging UI components.
     */
    private void configurePanels() {
        addPanel(languageLabel, languageBox);
        addPanel(designButton, randButton);
        addPanel(dimensionLabel, dimensionBox);
        addPanel(historyScrollPane);
        addPanel(timerLabel, timerBox);
        addPanel(resetButton);
        addPanel(startButton);
        setOpaqueAndMaxSize(panelList);
    }

    /**
     * Adds a new panel with the given components to the list of panels.
     * 
     * @param components The components to add to the panel.
     */
    private void addPanel(Component... components) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 600));
        for (Component component : components) {
            panel.add(component);
        }
        panelList.add(panel);
    }

    /**
     * Sets the opacity and maximum size for the panels in the list.
     * 
     * @param panels The list of panels to set opacity and maximum size for.
     */
    private void setOpaqueAndMaxSize(LinkedList<JPanel> panels) {
        for (JPanel panel : panels) {
            panel.setOpaque(false);
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 600));
        }
    }

    /**
     * Updates the timer display.
     */
    private void updateTimer() {
        elapsedTime += 1000;
        int hours = (int) elapsedTime / 3600000;
        int minutes = (int) (elapsedTime % 3600000) / 60000;
        int seconds = (int) ((elapsedTime % 3600000) % 60000) / 1000;
        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerBox.setText(timeString);
    }

    /**
     * Adds action listeners to various UI components within the MiddleMenu.
     */
    public void addComponentsToPanel() {
        languageBox.addActionListener(controller);
        dimensionBox.addActionListener(controller);
        resetButton.addActionListener(controller);
        startButton.addActionListener(controller);
        randButton.addActionListener(controller);
        designButton.addActionListener(controller);

        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 600));
        logoPanel.add(logoLabel);

        add(logoPanel);
        panelList.forEach(this::add);
    }

    /**
     * Sets the UI components' texts according to the selected language.
     */
    public void setLanguage() {
        String language = (String) languageBox.getSelectedItem();
        String filePath = String.format(
                "../resource/txt_%s.txt",
                language);

        ArrayList<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            languageLabel.setText(lines.get(0));
            designButton.setText(lines.get(1));
            randButton.setText(lines.get(2));
            dimensionLabel.setText(lines.get(3));
            timerLabel.setText(lines.get(4));
            resetButton.setText(lines.get(5));
            startButton.setText(lines.get(6));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the UI elements to their start state for gameplay.
     */
    public void setStartState() {
        timer.start();

        designButton.setEnabled(false);
        randButton.setEnabled(false);
        languageBox.setEnabled(false);
        dimensionBox.setEnabled(false);
    }

    /**
     * Sets the UI elements to their reset state.
     */
    public void setResetState() {
        stopTimer();
        elapsedTime = 0;
        timerBox.setText("00:00:00");

        designButton.setEnabled(true);
        randButton.setEnabled(true);
        languageBox.setEnabled(true);
        dimensionBox.setEnabled(true);
    }

    /**
     * Stops the timer that tracks elapsed time.
     */
    public void stopTimer() {
        timer.stop();
    }

    /**
     * Updates the history box with a new gameplay event.
     *
     * @param id         The player ID (1 or 2).
     * @param coordinate The coordinate where the event occurred.
     * @param result     The result of the event (hit or miss).
     */
    public void updateHistory(int id, String coordinate, String result) {
        String subject = id == 1 ? "Player 1" : "Player 2";

        String message = subject + ": " + coordinate + " (" + result + ")" + "\n"
                + historyBox.getText();

        historyBox.setText(message);
    }

    /**
     * Updates the history box with a new record. This function is called instead of
     * its other version in online mode,
     * to keep it simple the server will just return a record and this one will set
     * it
     *
     * @param record The record to be displayed in the history box.
     */
    public void updateHistory(String record) {
        historyBox.setText(record);
    }

    /**
     * Clears the history box.
     */
    public void clearHistory() {
        historyBox.setText("");
    }

    /**
     * Gets the "Start" button.
     *
     * @return The "Start" button.
     */
    public JButton getStartButton() {
        return startButton;
    }

    /**
     * Gets the "Random" button.
     *
     * @return The "Random" button.
     */
    public JButton getRandButton() {
        return randButton;
    }

    /**
     * Gets the dimension selection box.
     *
     * @return The dimension selection box.
     */
    public JComboBox<String> getDimensionBox() {
        return dimensionBox;
    }

    /**
     * Gets the "Reset" button.
     *
     * @return The "Reset" button.
     */
    public JButton getResetButton() {
        return resetButton;
    }

    /**
     * Gets the language selection box.
     *
     * @return The language selection box.
     */
    public JComboBox<String> getLanguageBox() {
        return languageBox;
    }

    /**
     * Gets the "Design" button.
     *
     * @return The "Design" button.
     */
    public JButton getDesignButton() {
        return designButton;
    }

    /**
     * Sets the UI elements to disabled state for online gameplay.
     */
    public void setOnlineGame() {
        designButton.setEnabled(false);
        startButton.setEnabled(false);
        randButton.setEnabled(false);
        resetButton.setEnabled(false);
        dimensionBox.setEnabled(false);

    }
}
