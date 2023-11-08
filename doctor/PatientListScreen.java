package doctor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class PatientListScreen extends JFrame {

    private JTable patientTable;
    private JScrollPane tableScrollPane;
    private JTextField filterTextField;

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
        tableScrollPane.getViewport().setBackground(new Color(221,221,221) );

        filterTextField = new JTextField(20);

        searchButton = new JButton("Search");
        searchButton.setForeground(new Color (255,255,255));
        searchButton.setBackground (new Color(42,127,186) );

        setLayout(new BorderLayout());
        tableScrollPane.setBackground(Color.BLACK);

        add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(filterTextField);
        buttonPanel.add(searchButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadPatientsFromFile("patient.txt");

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