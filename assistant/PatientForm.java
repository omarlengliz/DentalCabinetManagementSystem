package assistant;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
public class PatientForm extends JFrame {

    private JLabel nameLabel;
    private JTextField nameTextField;
    private JLabel lastnameLabel;
    private JTextField lastnameTextField;
    private JLabel ageLabel;
    private JTextField ageTextField;
    private JLabel dateLabel;
    private JTextField dateTextField;
    private JLabel jobLabel;
    private JTextField jobTextField;
    private JButton saveButton;
    private JButton cancelButton;
    public void populateFields(String name, String lastname, int age, String dateString, String job) {
        nameTextField.setText(name);
        lastnameTextField.setText(lastname);
        ageTextField.setText(String.valueOf(age));
        dateTextField.setText(dateString);
        jobTextField.setText(job);
    }
    public PatientForm(int add , PatientListScreen patientListScreen) {

        setTitle("Patient Form");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (screenSize.width - this.getWidth()) / 2;
        int centerY = (screenSize.height - this.getHeight()) / 2;

        setLocation(centerX, centerY);

        JPanel personalInfoPanel = new JPanel(new GridLayout(5, 2, 10, 30));
        personalInfoPanel.setBorder(new EmptyBorder(20,20,20,20)); 

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));


        nameLabel = new JLabel("Name:");
        nameTextField = new JTextField(20);
        lastnameLabel = new JLabel("Lastname:");
        lastnameTextField = new JTextField(20);
        ageLabel = new JLabel("Age:");
        ageTextField = new JTextField(20);
        dateLabel = new JLabel("Date:");
        dateTextField = new JTextField(20);
        jobLabel = new JLabel("Job:");

        jobTextField = new JTextField(20);
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        personalInfoPanel.add(nameLabel);
        personalInfoPanel.add(nameTextField);
        personalInfoPanel.add(lastnameLabel);
        personalInfoPanel.add(lastnameTextField);
        personalInfoPanel.add(ageLabel);
        personalInfoPanel.add(ageTextField);
        personalInfoPanel.add(dateLabel);
        personalInfoPanel.add(dateTextField);
        personalInfoPanel.add(jobLabel);
        personalInfoPanel.add(jobTextField);
        saveButton.setBackground (new Color(42,127,186) );
        saveButton.setForeground(new Color (255,255,255));
        cancelButton.setBackground (new Color(42,127,186) );
        cancelButton.setForeground(new Color (255,255,255));

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        setLayout(new BorderLayout(10, 30));

        personalInfoPanel.setBorder(new EmptyBorder(20,20,20,20)); 

        add(personalInfoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(600  , 400);

        setLocationRelativeTo(null);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String name = nameTextField.getText();
                String lastname = lastnameTextField.getText();
                int age = Integer.parseInt(ageTextField.getText());
                String dates = dateTextField.getText();
                String job = jobTextField.getText();

                String tempFilePath = "patient_temp.txt";
                boolean userExists = false;

                try (BufferedReader reader = new BufferedReader(new FileReader("patient.txt"));
                     PrintWriter writer = new PrintWriter(new FileWriter(tempFilePath))) {

                    String line;
                    int i=0; 
                    while ((line = reader.readLine()) != null) {

                        String[] patientData = line.split(",");
                        if (patientData.length >= 4) {

                            if (add == i ) {

                               writer.println(name + "," + lastname + "," + age + "," + dates + "," + job);
                                userExists = true;
                            } else {

                                writer.println(line);
                            }
                        }
                        i++;
                    }

                    if (!userExists) {
                        writer.println(name + "," + lastname + "," + age + "," + dates + "," + job);
                    }

                    writer.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(PatientForm.this, "Error saving patient data.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                try {
                    Files.move(Paths.get(tempFilePath), Paths.get("patient.txt"), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(PatientForm.this, "Error saving patient data.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                nameTextField.setText("");
                lastnameTextField.setText("");
                ageTextField.setText("");
                dateTextField.setText("");
                jobTextField.setText("");

                JOptionPane.showMessageDialog(PatientForm.this, "Patient data saved successfully!");
                dispose();
                patientListScreen.loadPatientsFromFile("patient.txt");
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                nameTextField.setText("");
                lastnameTextField.setText("");
                ageTextField.setText("");
                dateTextField.setText("");
                jobTextField.setText("");
            }
        });
    }

    public static void main(String[] args) {

    }
}