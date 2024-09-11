package org.example.ui;

import javax.swing.*;

public class LoaderDialog extends JDialog {
    private JProgressBar progressBar;

    public LoaderDialog(JFrame parent) {
        super(parent, "Processing...", true);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setString("Please wait...");
        progressBar.setStringPainted(true);

        this.add(progressBar);
        this.setSize(300, 100);
        this.setLocationRelativeTo(parent);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    public void showLoader() {
        SwingUtilities.invokeLater(() -> this.setVisible(true));
    }

    public void hideLoader() {
        SwingUtilities.invokeLater(() -> this.setVisible(false));
    }
}

