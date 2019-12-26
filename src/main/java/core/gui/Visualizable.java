package core.gui;

import javax.swing.*;

/**
 * Classes that implement this interface are able to create a visual component
 * that can be utilized and displayed by the view. It is optional for an environment
 * to defined a GUI (View is checking it via "instance of").
 * Furthermore a state implement this interface, so it can be displayed from the
 * state-action-table.
 */
public interface Visualizable {
    JComponent visualize();
}
