package com.vehicularcloud.gui;

import javax.swing.*;
import java.awt.*;

import com.vehicularcloud.controller.RequestType;

/**
 * VCRTS Milestone 5 – VC Controller GUI
 * Member 3: Sadia Sultana
 *
 * This is the GUI window for the VC Controller.
 * When a Task Owner or Vehicle Owner sends a request,
 * the server shows it here. The controller (human) must
 * click ACCEPT or REJECT. The server thread waits for
 * this decision before sending a final result back to the client.
 */

public class VCControllerGUI {

    // This list stores all incoming requests so the controller can see them
    private static DefaultListModel<String> listModel = new DefaultListModel<>();

    // JList is used for displaying the incoming requests on the screen
    private static JList<String> requestList = new JList<>(listModel);

    // This variable stores Accept (true) or Reject (false)
    // If decision = null → no button was clicked yet
    private static Boolean decision = null;

    // Called from the server to build and show the GUI
    public static void createAndShowGUI() {

        // Create window for the controller
        JFrame frame = new JFrame("VC Controller – Server Window");
        frame.setSize(650, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Title at the top
        JLabel title = new JLabel("Incoming Requests", SwingConstants.CENTER);

        // Scrollable area for request list
        JScrollPane scroll = new JScrollPane(requestList);

        // Buttons for Accept and Reject actions
        JButton acceptBtn = new JButton("ACCEPT");
        JButton rejectBtn = new JButton("REJECT");

        // When ACCEPT is clicked, set decision = true and wake the server thread
        acceptBtn.addActionListener(e -> {
            decision = true;
            synchronized (VCControllerGUI.class) {
                VCControllerGUI.class.notify();
            }
        });

        // When REJECT is clicked, set decision = false and wake the server thread
        rejectBtn.addActionListener(e -> {
            decision = false;
            synchronized (VCControllerGUI.class) {
                VCControllerGUI.class.notify();
            }
        });

        // Panel to hold the two buttons
        JPanel panel = new JPanel();
        panel.add(acceptBtn);
        panel.add(rejectBtn);

        // Set layout of the window
        frame.setLayout(new BorderLayout());
        frame.add(title, BorderLayout.NORTH);       // top
        frame.add(scroll, BorderLayout.CENTER);     // middle
        frame.add(panel, BorderLayout.SOUTH);       // bottom

        // Make the GUI visible
        frame.setVisible(true);
    }

    /**
     * Called by the server whenever a new request is received.
     * 1. Shows the request in the GUI list.
     * 2. Resets decision.
     * 3. Waits until ACCEPT or REJECT is clicked.
     * 4. Returns true if accepted, false if rejected.
     */
    public static boolean waitForDecision(RequestType type, String request) {

        // Add the request to the list so the controller can see it
        String requestEntry = type + " → " + request;
        listModel.addElement(requestEntry);

        // Reset decision so that previous clicks do NOT affect this request
        decision = null;

        // Server thread will wait here until Accept/Reject is chosen
        synchronized (VCControllerGUI.class) {
            while (decision == null) {
                try {
                    VCControllerGUI.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // Remove the request from the list after decision is made
        listModel.removeElement(requestEntry);

        // When decision is NOT null → return the final decision
        return decision;
    }
}
