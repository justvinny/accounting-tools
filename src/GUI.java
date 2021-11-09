import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GUI extends JFrame {
    private static final int FRAME_WIDTH = 500;
    private static final int FRAME_HEIGHT = 120;

    private SpringLayout layout;
    private JFileChooser fileChooser;
    private JLabel labelPath;
    private JTextField fieldPath;
    private JButton btnBrowse, btnConvert;
    private JProgressBar progressBar;

    public GUI() {
        frameSettings();

        // Init components
        fileChooser = new JFileChooser();
        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("PDF files only", "pdf");
        fileChooser.setFileFilter(fileFilter);

        labelPath = new JLabel("File Path: ");
        fieldPath = new JTextField();

        btnBrowse = new JButton("Browse");
        btnConvert = new JButton("Convert");

        progressBar = new JProgressBar();
        progressBar.setString("Converting...");

        // Listeners
        btnBrowse.addActionListener(e -> {
            fileChooser.showOpenDialog(getContentPane());
        });

        btnConvert.addActionListener(e -> {
            progressBar.setStringPainted(true);
            progressBar.setIndeterminate(true);
        });

        // Set layout constraints
        setLayoutConstraints();

        // Add components
        getContentPane().add(labelPath);
        getContentPane().add(fieldPath);
        getContentPane().add(btnBrowse);
        getContentPane().add(btnConvert);
        getContentPane().add(progressBar);
    }

    private void frameSettings() {
        layout = new SpringLayout();
        setTitle("Accounting Tools");
        getContentPane().setLayout(layout);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }

    private void setLayoutConstraints() {
        layout.putConstraint(SpringLayout.WEST, labelPath, 10, SpringLayout.WEST, getContentPane());
        layout.putConstraint(SpringLayout.NORTH, labelPath, 13, SpringLayout.NORTH, getContentPane());

        layout.putConstraint(SpringLayout.WEST, fieldPath, 10, SpringLayout.EAST, labelPath);
        layout.putConstraint(SpringLayout.EAST, fieldPath, -10, SpringLayout.WEST, btnBrowse);
        layout.putConstraint(SpringLayout.NORTH, fieldPath, 13, SpringLayout.NORTH, getContentPane());

        layout.putConstraint(SpringLayout.EAST, btnConvert, -10, SpringLayout.EAST, getContentPane());
        layout.putConstraint(SpringLayout.NORTH, btnConvert, 10, SpringLayout.NORTH, getContentPane());

        layout.putConstraint(SpringLayout.EAST, btnBrowse, -10, SpringLayout.WEST, btnConvert);
        layout.putConstraint(SpringLayout.NORTH, btnBrowse, 10, SpringLayout.NORTH, getContentPane());

        layout.putConstraint(SpringLayout.WEST, progressBar, 10, SpringLayout.WEST, getContentPane());
        layout.putConstraint(SpringLayout.EAST, progressBar, -10, SpringLayout.EAST, getContentPane());
        layout.putConstraint(SpringLayout.SOUTH, progressBar, -10, SpringLayout.SOUTH, getContentPane());
        layout.putConstraint(SpringLayout.NORTH, progressBar, 10, SpringLayout.SOUTH, btnConvert);
    }
}
