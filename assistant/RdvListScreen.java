package assistant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.io.FileWriter;
import java.io.PrintWriter;
public class RdvListScreen extends JFrame {

    private JTable rdvTable;
    private JScrollPane tableScrollPane;
    private JTextField filterTextField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton searchButton;

    private DefaultTableModel tableModel;

    public RdvListScreen() {
        CustomMenu customMenu = new CustomMenu(this);

        setJMenuBar(customMenu);

        setTitle("rdv List");
        setSize(600, 400);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (screenSize.width - this.getWidth()) / 2;
        int centerY = (screenSize.height - this.getHeight()) / 2;

        setLocation(centerX, centerY);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {"name", "date", "heure"};
        tableModel = new DefaultTableModel(columnNames, 0);
        rdvTable = new JTable(tableModel);
        rdvTable.getTableHeader().setBackground(new Color(73,83,90));

        rdvTable.getTableHeader().setForeground(Color.white);
        tableScrollPane = new JScrollPane(rdvTable);

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

        add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(filterTextField);
        buttonPanel.add(searchButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadRdvsFromFile("appointments.txt");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openrdvForm();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedrdv();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSelectedrdv();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterTable();
            }
        });
    }

    void loadRdvsFromFile(String filePath) {
        tableModel.setRowCount(0); 

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {

                String[] rdvData = line.split(",");
                if (rdvData.length == 3) {
                    String name = rdvData[0];
                    String date = rdvData[1];
                    String startTime = rdvData[2];

                    tableModel.addRow(new Object[]{name, date, startTime});
                }
            }
        } catch (IOException  ex) {

            System.out.println(ex.toString());

            JOptionPane.showMessageDialog(RdvListScreen.this, "Error loading rdv data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openrdvForm() {
        RdvForm rdvForm = new RdvForm(-1 ,this);
        rdvForm.setVisible(true);
    }

    private void deleteSelectedrdv() {
        int selectedRow = rdvTable.getSelectedRow();
        if (selectedRow >= 0) {

            try {
                File inputFile = new File("appointments.txt");
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
                    loadRdvsFromFile("appointments.txt");
                } else {
                    JOptionPane.showMessageDialog(RdvListScreen.this, "Error deleting rdv data.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(RdvListScreen.this, "Error deleting rdv data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateSelectedrdv() {
        int selectedRow = rdvTable.getSelectedRow();
        if (selectedRow >= 0) {
            String name = (String) tableModel.getValueAt(selectedRow, 0);
            String date = (String) tableModel.getValueAt(selectedRow, 1);
            String startTime = (String) tableModel.getValueAt(selectedRow, 2);

            RdvForm rdvForm = new RdvForm(selectedRow, this);
            rdvForm.populateFields(name, date, startTime);
            rdvForm.setVisible(true);
        }
    }

    private void filterTable() {
        String filterText = filterTextField.getText();
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(rdvTable.getModel());
        sorter.setRowFilter(RowFilter.regexFilter(filterText));
        rdvTable.setRowSorter(sorter);
    }

    public static void main(String[] args) {
        RdvListScreen rdvListScreen = new RdvListScreen();
        rdvListScreen.setVisible(true);
    }
}