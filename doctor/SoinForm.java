package doctor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class SoinForm extends JFrame {

    private JComboBox<String> patientComboBox;
    private JComboBox<String> soinComboBox;
    private JTextArea noteTextArea;
    private JButton saveButton;
    private JButton cancelButton;
    String[] options = {
        "détartrage",
        "plombage",
        "dévitalisation dentaire",
        "extraction dentaire",
        "scellement des sillons"
    };
    public  void populateFields(String name, String soin ,String note) {
        patientComboBox.setSelectedItem(name);
        soinComboBox.setSelectedItem(soin);
        noteTextArea.setText(note);

    }
    public SoinForm(int add  , SoinListScreen soinListScreen) {
        setTitle("Soins Form");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (screenSize.width - this.getWidth()) / 2;
        int centerY = (screenSize.height - this.getHeight()) / 2;

        setLocation(centerX, centerY);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 30));
        formPanel.setBorder(new EmptyBorder(20,20,20,20)); 

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JLabel patientLabel = new JLabel("Patient:");
        patientComboBox = new JComboBox<>();
        loadPatientNamesFromFile("patient.txt"); 

        JLabel soinLabel = new JLabel("soin:");
        soinComboBox = new   JComboBox<>(options);

        JLabel noteLabel = new JLabel("note:");
        noteTextArea = new JTextArea(5,50);

        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        saveButton.setBackground (new Color(42,127,186) );
        saveButton.setForeground(new Color (255,255,255));
        cancelButton.setBackground (new Color(42,127,186) );
        cancelButton.setForeground(new Color (255,255,255));

        formPanel.add(patientLabel);
        formPanel.add(patientComboBox);
        formPanel.add(soinLabel);
        formPanel.add(soinComboBox);
        formPanel.add(noteLabel);
        formPanel.add(noteTextArea);

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        setLayout(new BorderLayout(10, 10));

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(600, 400);

        setLocationRelativeTo(null);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String patient = (String) patientComboBox.getSelectedItem();
                String soin = (String) soinComboBox.getSelectedItem();
                String note = noteTextArea.getText();

                String tempFilePath = "temp.txt";
                boolean soinExists = false;

                try (BufferedReader reader = new BufferedReader(new FileReader("soins.txt"));
                     PrintWriter writer = new PrintWriter(new FileWriter(tempFilePath))) {

                    String line;
                    int i=0; 
                    while ((line = reader.readLine()) != null) {

                        String[] patientData = line.split(",");
                        if (patientData.length >0) {

                            if (add == i ) {

                               writer.println(patient + "," + soin + "," + note);
                                soinExists = true;
                            } else {

                                writer.println(line);
                            }
                        }
                        i++;
                    }

                    if (!soinExists) {
                        writer.println(patient + "," + soin + "," + note );
                    }

                    writer.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(SoinForm.this, "Error saving soin data.", "Error", JOptionPane.ERROR_MESSAGE);

                }

                try {
                    Files.move(Paths.get(tempFilePath), Paths.get("soins.txt"), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(SoinForm.this, "Error saving soin data.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                JOptionPane.showMessageDialog(SoinForm.this, "soin data saved successfully!");
                soinListScreen.loadsoinsFromFile("soins.txt");

                dispose();

            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                patientComboBox.setSelectedIndex(0);
                soinComboBox.setSelectedIndex(0);
                noteTextArea.setText("");
            }
        });
    }

    private void loadPatientNamesFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] patientDetails = line.split(",");
                String patientName = patientDetails[0] + " " + patientDetails[1];
                patientComboBox.addItem(patientName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SoinForm SoinForm = new SoinForm(0,null);
        SoinForm.setVisible(true);
    }
}