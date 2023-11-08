package doctor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
public class RdvListScreen extends JFrame {

    private JTable rdvTable;
    private JScrollPane tableScrollPane;
    private JTextField filterTextField;
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
        rdvTable .getTableHeader().setBackground(new Color(73,83,90));

        rdvTable.getTableHeader().setForeground(Color.white);
        tableScrollPane = new JScrollPane(rdvTable);
        tableScrollPane.setBackground(Color.black);
        tableScrollPane.getViewport().setBackground(new Color(221,221,221) );

        filterTextField = new JTextField(20);

        searchButton = new JButton("Search");
        searchButton.setBackground (new Color(42,127,186) );
        searchButton.setForeground(Color.white);

        setLayout(new BorderLayout());

        add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(filterTextField);
        buttonPanel.add(searchButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadRdvsFromFile("appointments.txt");

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