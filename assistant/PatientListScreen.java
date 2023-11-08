package assistant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.io.FileWriter;
import java.io.PrintWriter;

public class PatientListScreen extends JFrame {

    private JTable patientTable;
    private JScrollPane tableScrollPane;
    private JTextField filterTextField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton searchButton;

    private DefaultTableModel tableModel;

    public PatientListScreen() {
        CustomMenu customMenu = new CustomMenu(this);

        setJMenuBar(customMenu);

        setTitle("Patient List");
        setBackground(Color.black);

        setSize(600, 400);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (screenSize.width - this.getWidth()) / 2;
        int centerY = (screenSize.height - this.getHeight()) / 2;

        setLocation(centerX, centerY);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {"Name", "Lastname", "Age", "Date", "Job"};
        tableModel = new DefaultTableModel(columnNames, 0);

        patientTable = new JTable(tableModel);
        patientTable.getTableHeader().setBackground(new Color(73,83,90));

        patientTable.getTableHeader().setForeground(Color.white);

        tableScrollPane = new JScrollPane(patientTable);
        tableScrollPane.setBackground(Color.black);

        filterTextField = new JTextField(20);
        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        updateButton = new JButton("Update");
        searchButton = new JButton("Search");
        addButton.setBackground (new Color(42,127,186) );
        addButton.setForeground(new Color (255,255,255));
        deleteButton.setBackground (new Color(42,127,186) );
        deleteButton.setForeground(new Color (255,255,255));
        updateButton.setBackground (new Color(42,127,186) );
        updateButton.setForeground(new Color (255,255,255));
        searchButton.setBackground (new Color(42,127,186) );
        searchButton.setForeground(new Color (255,255,255));
        setLayout(new BorderLayout());
        tableScrollPane.setBackground(Color.BLACK);

        add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(filterTextField);
        buttonPanel.add(searchButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadPatientsFromFile("patient.txt");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPatientForm();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedPatient();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSelectedPatient();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterTable();
            }
        });
    }

    void loadPatientsFromFile(String filePath) {
        tableModel.setRowCount(0); 
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(patientTable.getModel());
        patientTable.setRowSorter(sorter);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {

                String[] patientData = line.split(",");
                if (patientData.length == 5) {
                    String name = patientData[0];
                    String lastname = patientData[1];
                    int age = Integer.parseInt(patientData[2]);
                    String date = (patientData[3]);
                    String job = patientData[4];

                    tableModel.addRow(new Object[]{name, lastname, age, (date), job});
                }
            }
        } catch (IOException  ex) {

            System.out.println(ex.toString());

            JOptionPane.showMessageDialog(PatientListScreen.this, "Error loading patient data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openPatientForm() {
        PatientForm patientForm = new PatientForm(-1 ,this);
        patientForm.setVisible(true);
    }

    private void deleteSelectedPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow >= 0) {

            try {
                File inputFile = new File("patient.txt");
                File tempFile = new File("temp.txt");

                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

                String line;
                int i=-1;
                while ((line = reader.readLine()) != null) {
                        i++;

                        if (i==selectedRow) {
                            continue;
                        }
                        writer.println(line);

                    }

                writer.close();
                reader.close();

                if (inputFile.delete()) {
                    tempFile.renameTo(inputFile);
                    loadPatientsFromFile("patient.txt");
                } else {
                    JOptionPane.showMessageDialog(PatientListScreen.this, "Error deleting patient data.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(PatientListScreen.this, "Error deleting patient data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateSelectedPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow >= 0) {
            String name = (String) tableModel.getValueAt(selectedRow, 0);
            String lastname = (String) tableModel.getValueAt(selectedRow, 1);
            int age = (int) tableModel.getValueAt(selectedRow, 2);
            String dateString = (String) tableModel.getValueAt(selectedRow, 3);
            String job = (String) tableModel.getValueAt(selectedRow, 4);

            PatientForm patientForm = new PatientForm(selectedRow, this);
            patientForm.populateFields(name, lastname, age, dateString, job);
            patientForm.setVisible(true);
        }
    }

    private void filterTable() {
        String filterText = filterTextField.getText();
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(patientTable.getModel());
        sorter.setRowFilter(RowFilter.regexFilter(filterText));
        patientTable.setRowSorter(sorter);
    }

    public static void main(String[] args) {
        PatientListScreen patientListScreen = new PatientListScreen();
        patientListScreen.setVisible(true);
    }
}
 class CustomCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (column == 1) {

        } else {

            component.setBackground(table.getBackground());
        }

        return component;
    }
}