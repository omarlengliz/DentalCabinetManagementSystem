package doctor;

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
public class SoinListScreen extends JFrame {

    private JTable soinTable;
    private JScrollPane tableScrollPane;
    private JTextField filterTextField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton searchButton;

    private DefaultTableModel tableModel;

    public SoinListScreen() {
        CustomMenu customMenu = new CustomMenu(this);

        setJMenuBar(customMenu);

        setTitle("Soin List");
        setSize(600, 400);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (screenSize.width - this.getWidth()) / 2;
        int centerY = (screenSize.height - this.getHeight()) / 2;

        setLocation(centerX, centerY);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {"patient ", "soin ", "note"};
        tableModel = new DefaultTableModel(columnNames, 0);
        soinTable = new JTable(tableModel);
        soinTable .getTableHeader().setBackground(new Color(73,83,90));

        soinTable.getTableHeader().setForeground(Color.white);

        tableScrollPane = new JScrollPane(soinTable);

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

        loadsoinsFromFile("soins.txt");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSoinForm();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedsoin();
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

    void loadsoinsFromFile(String filePath) {
        tableModel.setRowCount(0); 

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {

                String[] rdvData = line.split(",");
                if (rdvData.length == 3) {
                    String patient = rdvData[0];
                    String soin =  rdvData[1];
                    String note = rdvData[2];

                    tableModel.addRow(new Object[]{patient, soin, note});
                }
            }
        } catch (IOException  ex) {

            System.out.println(ex.toString());

            JOptionPane.showMessageDialog(SoinListScreen.this, "Error loading soin data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openSoinForm() {
        SoinForm SoinForm = new SoinForm(-1 ,this);
        SoinForm.setVisible(true);
    }

    private void deleteSelectedsoin() {
        int selectedRow = soinTable.getSelectedRow();
        if (selectedRow >= 0) {

            try {
                File inputFile = new File("soins.txt");
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
                    loadsoinsFromFile("soins.txt");
                } else {
                    JOptionPane.showMessageDialog(SoinListScreen.this, "Error deleting soin data.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(SoinListScreen.this, "Error deleting soin data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateSelectedrdv() {
        int selectedRow = soinTable.getSelectedRow();
        if (selectedRow >= 0) {
            String patient = (String) tableModel.getValueAt(selectedRow, 0);
            String soin =  (String) tableModel.getValueAt(selectedRow, 1);
            String note = (String) tableModel.getValueAt(selectedRow, 2);

            SoinForm SoinForm = new SoinForm(selectedRow, this);
            SoinForm.populateFields(patient, soin, note);
            SoinForm.setVisible(true);
        }
    }

    private void filterTable() {
        String filterText = filterTextField.getText();
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(soinTable.getModel());
        sorter.setRowFilter(RowFilter.regexFilter(filterText));
        soinTable.setRowSorter(sorter);
    }

    public static void main(String[] args) {
        SoinListScreen SoinListScreen = new SoinListScreen();
        SoinListScreen.setVisible(true);
    }
}