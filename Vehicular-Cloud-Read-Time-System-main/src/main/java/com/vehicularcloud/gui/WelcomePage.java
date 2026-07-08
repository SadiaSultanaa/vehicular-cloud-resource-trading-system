package com.vehicularcloud.gui;

import javax.swing.*;
import java.awt.*;
import com.vehicularcloud.validation.InputController;

/**
 * Welcome page displayed at application startup
 * Provides an introduction to the Vehicular Cloud Console
 */
public class WelcomePage extends JFrame {
    private InputController inputController;

    public WelcomePage(InputController inputController) {
        this.inputController = inputController;
        
        // Frame setup
        setTitle("Welcome - Vehicular Cloud Console");
        setSize(700, 650);
        setMinimumSize(new Dimension(700, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Create components
        createContent();
        
        setVisible(true);
    }
    
    private void createContent() {
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(52, 152, 219);
                Color color2 = new Color(41, 128, 185);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Add padding above title
        gbc.gridy = 0;
        mainPanel.add(Box.createVerticalStrut(50), gbc);
        
        // App title
        JLabel titleLabel = new JLabel("Vehicular Cloud Console");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        mainPanel.add(titleLabel, gbc);
        
        // Add some spacing
        gbc.gridy = 2;
        mainPanel.add(Box.createVerticalStrut(30), gbc);
        
        // Description panel with white background
        JPanel descPanel = new JPanel();
        descPanel.setBackground(Color.WHITE);
        descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.Y_AXIS));
        descPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185), 2),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        
        // Description text
        JLabel desc1 = new JLabel("A comprehensive system for managing vehicular cloud resources");
        desc1.setFont(new Font("Arial", Font.PLAIN, 16));
        desc1.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        descPanel.add(desc1);
        descPanel.add(Box.createVerticalStrut(25));
        
        // Features
        JLabel featuresTitle = new JLabel("Features:");
        featuresTitle.setFont(new Font("Arial", Font.BOLD, 18));
        featuresTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        descPanel.add(featuresTitle);
        descPanel.add(Box.createVerticalStrut(15));
        
        String[] features = {
            "• Vehicle Owner Registration - Register your vehicle in the cloud system",
            "• Client Job Submission - Submit computational jobs to the vehicular cloud",
            "• VC Controller - Calculate job completion times using FIFO scheduling",
            "• Data Persistence - All transactions are saved to CSV files"
        };
        
        for (String feature : features) {
            JLabel featureLabel = new JLabel(feature);
            featureLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            featureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            descPanel.add(featureLabel);
            descPanel.add(Box.createVerticalStrut(8));
        }
        
        gbc.gridy = 3;
        mainPanel.add(descPanel, gbc);
        
        // Add spacing
        gbc.gridy = 4;
        mainPanel.add(Box.createVerticalStrut(30), gbc);
        
        // Get Started button
        JButton getStartedButton = new JButton("Get Started");
        getStartedButton.setFont(new Font("Arial", Font.BOLD, 18));
        getStartedButton.setBackground(new Color(46, 204, 113));
        getStartedButton.setForeground(Color.WHITE);
        getStartedButton.setFocusPainted(false);
        getStartedButton.setBorderPainted(false);
        getStartedButton.setPreferredSize(new Dimension(200, 50));
        getStartedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        getStartedButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                getStartedButton.setBackground(new Color(39, 174, 96));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                getStartedButton.setBackground(new Color(46, 204, 113));
            }
        });
        
        getStartedButton.addActionListener(e -> {
            // Close welcome page and open main application
            dispose();
            new VehicularCloudGUI(inputController);
        });
        
        gbc.gridy = 5;
        mainPanel.add(getStartedButton, gbc);
        
        // Exit button
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 14));
        exitButton.setBackground(new Color(231, 76, 60));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBorderPainted(false);
        exitButton.setPreferredSize(new Dimension(120, 35));
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        exitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitButton.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitButton.setBackground(new Color(231, 76, 60));
            }
        });
        
        exitButton.addActionListener(e -> System.exit(0));
        
        gbc.gridy = 6;
        mainPanel.add(exitButton, gbc);
        
        // Footer
        JLabel footerLabel = new JLabel("Version 1.0 - Team: Christopher, Ryan, Sadia, Rezwan");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        footerLabel.setForeground(new Color(236, 240, 241));
        gbc.gridy = 7;
        gbc.insets = new Insets(20, 20, 10, 20);
        mainPanel.add(footerLabel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }
}

