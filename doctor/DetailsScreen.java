package doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Dimension;


public class DetailsScreen extends JFrame {
    private JComboBox<String> selectBox;
    private JTable detailsTable;

    public DetailsScreen() {
        setTitle("Details Screen");
        setSize(600, 400);

        selectBox = new JComboBox<>();
        loadUserNamesFromFile("patient.txt"); 
        selectBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedUser = (String) selectBox.getSelectedItem();
                displayUserDetails(selectedUser);
            }
        });

        detailsTable = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"", "values"}, 0);
        detailsTable.setModel(tableModel);

        detailsTable.setRowHeight(30);

        JScrollPane tableScrollPane = new JScrollPane(detailsTable);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(selectBox, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(600, 200));
        getContentPane().add(panel, BorderLayout.NORTH);

        JButton close = new JButton("Close");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(close);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        close.setPreferredSize(new Dimension(100, 30));
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

       




        JPanel lowerPanel = new JPanel();
        lowerPanel.setBackground(Color.WHITE);

        getContentPane().add(lowerPanel);

        setVisible(true);
    }

    private void loadUserNamesFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                String userName = userDetails[0] + " " + userDetails[1]; 
                selectBox.addItem(userName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayUserDetails(String user) {
        DefaultTableModel tableModel = (DefaultTableModel) detailsTable.getModel();
        tableModel.setRowCount(0);

        try (BufferedReader reader = new BufferedReader(new FileReader("patient.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                String userName = userDetails[0] + " " + userDetails[1]; // Combine first name and last name
                if (userName.equals(user)) {
                    tableModel.addRow(new Object[]{"Name", userDetails[0]});
                    tableModel.addRow(new Object[]{"Last Name", userDetails[1]});
                    tableModel.addRow(new Object[]{"Age", userDetails[2]});
                    tableModel.addRow(new Object[]{"Date of Birth", userDetails[3]});
                    tableModel.addRow(new Object[]{"Job", userDetails[4]});
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DetailsScreen());
    }
}

