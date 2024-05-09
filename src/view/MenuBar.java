package view;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import control.Controller;

/**
 * The MenuBar class represents the menu bar in the game's user interface.
 * It contains various menus and menu items for game control and settings.
 * Extends JMenuBar.
 * 
 * @author Gia Bao Tran - Kiet Tran
 */
public class MenuBar extends JMenuBar {

    // File menu components
    private JMenu gameMenu = new JMenu("Game");
    private JMenuItem newItem = new JMenuItem("New");
    private JMenuItem solutionItem = new JMenuItem("Solution");
    private JMenuItem exitItem = new JMenuItem("Exit");

    // Help menu components
    private JMenu editMenu = new JMenu("Edit");
    private JMenuItem hitColor = new JMenuItem("Hit Color");
    private JMenuItem missedColor = new JMenuItem("Missed Color");
    private JMenuItem unselectedColor = new JMenuItem("Unselected Color");
    private JMenuItem abortItem = new JMenuItem("Abort");
    private JMenuItem guideItem = new JMenuItem("Guide");

    /**
     * Constructs a new MenuBar instance.
     * 
     * @param controller The Controller instance representing the game's controller.
     */
    public MenuBar(Controller controller) {
        gameMenu.add(newItem);
        gameMenu.add(solutionItem);
        gameMenu.add(exitItem);

        newItem.addActionListener(controller);
        solutionItem.addActionListener(controller);
        exitItem.addActionListener(controller);
        hitColor.addActionListener(controller);
        guideItem.addActionListener(controller);
        missedColor.addActionListener(controller);

        unselectedColor.addActionListener(controller);

        editMenu.add(hitColor);
        editMenu.add(missedColor);
        editMenu.add(unselectedColor);
        editMenu.add(abortItem);
        editMenu.add(guideItem);

        add(gameMenu);
        add(editMenu);
    }

    /**
     * Returns the "New" menu item.
     * 
     * @return The "New" menu item.
     */
    public JMenuItem getNewItem() {
        return newItem;
    }

    /**
     * Returns the "Solution" menu item.
     * 
     * @return The "Solution" menu item.
     */
    public JMenuItem getSolutionItem() {
        return solutionItem;
    }

    /**
     * Returns the "Exit" menu item.
     * 
     * @return The "Exit" menu item.
     */
    public JMenuItem getExitItem() {
        return exitItem;
    }

    /**
     * Returns the "Hit Color" menu item.
     * 
     * @return The "Hit Color" menu item.
     */
    public JMenuItem getHitColorItem() {
        return hitColor;
    }

    /**
     * Returns the "Missed Color" menu item.
     * 
     * @return The "Missed Color" menu item.
     */
    public JMenuItem getMissedColorItem() {
        return missedColor;
    }

    /**
     * Returns the "Unselected Color" menu item.
     * 
     * @return The "Unselected Color" menu item.
     */
    public JMenuItem getUnselectedColorItem() {
        return unselectedColor;
    }

    /**
     * Returns the "Abort" menu item.
     * 
     * @return The "Abort" menu item.
     */
    public JMenuItem getAbortItem() {
        return abortItem;
    }

    /**
     * Returns the "Guide" menu item.
     * 
     * @return The "Guide" menu item.
     */
    public JMenuItem getGuideItem() {
        return guideItem;
    }

}
