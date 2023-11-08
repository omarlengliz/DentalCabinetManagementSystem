package assistant;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class RdvForm extends JFrame {

    private JComboBox<String> patientComboBox;
    private JTextField dateTextField;
    private JTextField timeTextField;
    private JButton saveButton;
    private JButton cancelButton;
    public  void populateFields(String name, String date,String time) {
        patientComboBox.setSelectedItem(name);
        dateTextField.setText(date);
        timeTextField.setText(time);

    }
    public RdvForm(int add  , RdvListScreen rdvListScreen) {
        setTitle("rdv Form");
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

        JLabel dateLabel = new JLabel("Date:");
        dateTextField = new JTextField(20);

        JLabel timeLabel = new JLabel("Time:");
        timeTextField = new JTextField(20);

        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        formPanel.add(patientLabel);
        formPanel.add(patientComboBox);
        formPanel.add(dateLabel);
        formPanel.add(dateTextField);
        formPanel.add(timeLabel);
        formPanel.add(timeTextField);
        saveButton.setBackground (new Color(42,127,186) );
        saveButton.setForeground(new Color (255,255,255));
        cancelButton.setBackground (new Color(42,127,186) );
        cancelButton.setForeground(new Color (255,255,255));
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
                String date = dateTextField.getText();
                String time = timeTextField.getText();

                String tempFilePath = "temp.txt";
                boolean rdvExists = false;

                try (BufferedReader reader = new BufferedReader(new FileReader("appointments.txt"));
                     PrintWriter writer = new PrintWriter(new FileWriter(tempFilePath))) {

                    String line;
                    int i=0; 
                    while ((line = reader.readLine()) != null) {

                        String[] patientData = line.split(",");
                        if (patientData.length >0) {

                            if (add == i ) {

                               writer.println(patient + "," + date + "," + time);
                                rdvExists = true;
                            } else {

                                writer.println(line);
                            }
                        }
                        i++;
                    }

                    if (!rdvExists) {
                        writer.println(patient + "," + date + "," + time );
                    }

                    writer.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(RdvForm.this, "Error saving rdv data.", "Error", JOptionPane.ERROR_MESSAGE);

                }

                try {
                    Files.move(Paths.get(tempFilePath), Paths.get("appointments.txt"), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(RdvForm.this, "Error saving rdv data.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                JOptionPane.showMessageDialog(RdvForm.this, "rdv data saved successfully!");
                rdvListScreen.loadRdvsFromFile("appointments.txt");

                dispose();

            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                patientComboBox.setSelectedIndex(0);
                dateTextField.setText("");
                timeTextField.setText("");
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
        RdvForm rdvForm = new RdvForm(0,null);
        rdvForm.setVisible(true);
    }
}