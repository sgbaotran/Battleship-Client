package view;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import control.Controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The main menu GUI frame for the Battleship game.
 */
public class MainMenu extends JFrame {
    private ImageIcon imageIcon;
    private JPanel panel;
    private JButton singleButton;
    private JButton multiButton;
    private JButton extrasButton;

    private Controller controller;

    /**
     * Initializes the main menu frame with its components.
     */
    public MainMenu() {
        setTitle("Battleship");
        setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        setPanel();
        setSize(new Dimension(540, 350));
        setResizable(false);
        setVisible(true);
    }

    /**
     * Sets up the main panel of the main menu.
     */
    public void setPanel() {
        panel = new JPanel(new FlowLayout());
        panel.setPreferredSize(new Dimension(540, 350));
        panel.setBackground(new Color(158, 159, 165));
        setImage();
        setButton();
        add(panel);
    }

    /**
     * Loads and displays the image on the main menu.
     */
    public void setImage() {
        try {
            imageIcon = new ImageIcon(getClass().getResource("../images/game_about.jpg"));
            panel.add(new JLabel(imageIcon));
        } catch (Exception ex) {
            System.out.println("Image not found");
        }
    }

    /**
     * Sets up the buttons on the main menu.
     */
    public void setButton() {
        singleButton = new JButton("Single-player");
        multiButton = new JButton("Multi-player");
        extrasButton = new JButton("Extras");
        panel.add(singleButton);
        panel.add(multiButton);
        panel.add(extrasButton);
    }

    /**
     * Adds the provided controller to handle button actions.
     *
     * @param controller The controller to be added.
     */
    public void addController(Controller controller) {
        this.controller = controller;
        singleButton.addActionListener(controller);
        multiButton.addActionListener(controller);
        extrasButton.addActionListener(controller);
    }

    /**
     * Retrieves the single-player button.
     *
     * @return The single-player button.
     */
    public JButton getSingleButton() {
        return singleButton;
    }

    /**
     * Retrieves the multi-player button.
     *
     * @return The multi-player button.
     */
    public JButton getMultiButton() {
        return multiButton;
    }

    /**
     * Retrieves the extras button.
     *
     * @return The extras button.
     */
    public JButton getExtrasButton() {
        return extrasButton;
    }

    /**
     * Disposes of the main menu frame.
     */
    public void disposeMenu() {
        dispose();
    }
}
