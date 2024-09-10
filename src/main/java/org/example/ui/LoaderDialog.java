package org.example.ui;

import javax.swing.*;

public class LoaderDialog extends JDialog {
    private JProgressBar progressBar;

    public LoaderDialog(JFrame parent) {
        super(parent, "Processing...", true);  // Create a modal dialog
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);  // Set the progress bar to indeterminate mode
        progressBar.setString("Please wait...");
        progressBar.setStringPainted(true);

        this.add(progressBar);
        this.setSize(300, 100);  // Set size of the dialog
        this.setLocationRelativeTo(parent);  // Center the dialog on the parent frame
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);  // Prevent closing while loading
    }

    // Method to show the loader
    public void showLoader() {
        SwingUtilities.invokeLater(() -> this.setVisible(true));  // Show the dialog
    }

    // Method to hide the loader
    public void hideLoader() {
        SwingUtilities.invokeLater(() -> this.setVisible(false));  // Hide the dialog
    }
}

