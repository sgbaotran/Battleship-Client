package view;

import javax.swing.*;
import java.awt.*;
import control.Controller;

/**
 * The OnlineMenu class represents the user interface for the online game mode.
 */
public class OnlineMenu extends JFrame {
    private ImageIcon imageIcon;
    private JPanel panel;
    private JTextField userNameInput;
    private JTextField urlInput;
    private JTextField portTextField;
    private JTextArea messageBox;
    private JButton connectButton;
    private JButton endButton;

    private Controller controller;

    /**
     * Creates a new OnlineMenu instance.
     */
    public OnlineMenu() {
        setTitle("Battleship");
        setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        setPanel();
        setSize(new Dimension(540, 400));
        setResizable(false);
        setVisible(true);
    }

    /**
     * Configures and sets up the panel and its components.
     */
    public void setPanel() {
        panel = new JPanel(new FlowLayout());
        panel.setPreferredSize(new Dimension(540, 400));
        panel.setBackground(new Color(158, 159, 165));
        setImage();
        setInputComponents();
        add(panel);
    }

    /**
     * Adds the controller to handle user interactions with the buttons.
     * 
     * @param controller The controller to be added.
     */
    public void addController(Controller controller) {
        this.controller = controller;
        connectButton.addActionListener(controller);
        endButton.addActionListener(controller);
    }

    /**
     * Loads and displays the image icon on the panel.
     */
    public void setImage() {
        try {
            imageIcon = new ImageIcon(getClass().getResource("../images/client.png"));
            panel.add(new JLabel(imageIcon));
        } catch (Exception ex) {
            System.out.println("Image not found");
        }
    }

    /**
     * Sets up the input components for user interaction.
     */
    public void setInputComponents() {
        messageBox = new JTextArea(8, 42);
        JScrollPane messageScrollPane = new JScrollPane(messageBox);

        panel.add(new JLabel("User:"));
        userNameInput = new JTextField(5);
        panel.add(userNameInput);

        panel.add(new JLabel("URL:"));
        urlInput = new JTextField(8);
        panel.add(urlInput);

        panel.add(new JLabel("Port:"));
        portTextField = new JTextField(5);
        panel.add(portTextField);

        connectButton = new JButton("Connect");
        panel.add(connectButton);

        endButton = new JButton("End");
        panel.add(endButton);

        panel.add(messageScrollPane);
    }

    /**
     * Retrieves the connect button.
     * 
     * @return The connect button.
     */
    public JButton getConnectButton() {
        return connectButton;
    }

    /**
     * Retrieves the end button.
     * 
     * @return The end button.
     */
    public JButton getEndButton() {
        return endButton;
    }

    /**
     * Retrieves the URL input value.
     * 
     * @return The URL input value.
     */
    public String getUrl() {
        return urlInput.getText();
    }

    /**
     * Retrieves the port input value.
     * 
     * @return The port input value.
     */
    public String getPort() {
        return portTextField.getText();
    }

    /**
     * Retrieves the user name input value.
     * 
     * @return The user name input value.
     */
    public String getName() {
        return userNameInput.getText();
    }
}
