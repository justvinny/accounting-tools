import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.prefs.Preferences;

public class GUI extends JFrame {
    // Constants
    private static final int FRAME_WIDTH = 550;
    private static final int FRAME_HEIGHT = 120;
    private static final String LAST_DIRECTORY_KEY = "LastDirectoryID";

    // Fields
    private SpringLayout layout;
    private JFileChooser fileChooser;
    private JLabel labelPath;
    private JTextField fieldPath;
    private JButton btnBrowse;
    private JButton btnConvert;
    private JProgressBar progressBar;
    private File selectedFile;
    private Preferences preferences;
    private String lastSavedDirectory;

    public GUI() {
        frameSettings();
        initPreferences();
        initComponents();
        addListeners();
        setLayoutConstraints();
        addComponents();
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

    private void initPreferences() {
        preferences = Preferences.userRoot().node(this.getClass().getName());
        lastSavedDirectory = preferences.get(LAST_DIRECTORY_KEY, "");
    }

    private void initComponents() {
        fileChooser = new JFileChooser(lastSavedDirectory.isEmpty() ? null : lastSavedDirectory);
        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("PDF Files", "pdf");
        fileChooser.setFileFilter(fileFilter);
        labelPath = new JLabel("File Path: ");
        fieldPath = new JTextField();
        btnBrowse = new JButton("Browse");
        btnConvert = new JButton("Convert");
        progressBar = new JProgressBar();
        progressBar.setString("No Task");
        progressBar.setStringPainted(true);
    }

    private void addListeners() {
        // Open file explorer to choose a PDF to extract data from.
        btnBrowse.addActionListener(e -> {
            fileChooser.showOpenDialog(getContentPane());

            selectedFile = fileChooser.getSelectedFile();
            if (selectedFile != null) {
                preferences.put(LAST_DIRECTORY_KEY, selectedFile.getParent());
                fieldPath.setText(selectedFile.getPath());
            }
        });

        // Get input from acro forms from PDF and transform to CSV.
        btnConvert.addActionListener(e -> {
            String textInput = fieldPath.getText();
            if (textInput.length() > 0 && selectedFile != null) {
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        toggleLoading();
                        List<List<String>> formEntries = PDFUtilities.getFormEntries(selectedFile);

                        try {
                            Calculations calculations = new Calculations(formEntries);
                            CSVUtilities.writeToCSV(calculations.toString(), selectedFile);
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(getContentPane(), ex.getMessage(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        return null;
                    }

                    @Override
                    protected void done() {
                        super.done();
                        toggleLoading();
                    }
                }.execute();
            }
        });
    }

    private void toggleLoading() {
        fieldPath.setEnabled(!fieldPath.isEnabled());
        progressBar.setIndeterminate(!progressBar.isIndeterminate());

        if (progressBar.isIndeterminate()) {
            progressBar.setString("Processing...");
        } else {
            progressBar.setString("No Task");
        }
    }

    private void addComponents() {
        getContentPane().add(labelPath);
        getContentPane().add(fieldPath);
        getContentPane().add(btnBrowse);
        getContentPane().add(btnConvert);
        getContentPane().add(progressBar);
    }

    private void setLayoutConstraints() {
        // Label
        layout.putConstraint(SpringLayout.WEST, labelPath, 10, SpringLayout.WEST, getContentPane());
        layout.putConstraint(SpringLayout.NORTH, labelPath, 13, SpringLayout.NORTH, getContentPane());

        // Text Field
        layout.putConstraint(SpringLayout.WEST, fieldPath, 10, SpringLayout.EAST, labelPath);
        layout.putConstraint(SpringLayout.EAST, fieldPath, -10, SpringLayout.WEST, btnBrowse);
        layout.putConstraint(SpringLayout.NORTH, fieldPath, 13, SpringLayout.NORTH, getContentPane());

        // Buttons
        layout.putConstraint(SpringLayout.EAST, btnConvert, -10, SpringLayout.EAST, getContentPane());
        layout.putConstraint(SpringLayout.NORTH, btnConvert, 10, SpringLayout.NORTH, getContentPane());
        layout.putConstraint(SpringLayout.EAST, btnBrowse, -10, SpringLayout.WEST, btnConvert);
        layout.putConstraint(SpringLayout.NORTH, btnBrowse, 10, SpringLayout.NORTH, getContentPane());

        // Progress bar
        layout.putConstraint(SpringLayout.WEST, progressBar, 10, SpringLayout.WEST, getContentPane());
        layout.putConstraint(SpringLayout.EAST, progressBar, -10, SpringLayout.EAST, getContentPane());
        layout.putConstraint(SpringLayout.SOUTH, progressBar, -10, SpringLayout.SOUTH, getContentPane());
        layout.putConstraint(SpringLayout.NORTH, progressBar, 10, SpringLayout.SOUTH, btnConvert);
    }
}
