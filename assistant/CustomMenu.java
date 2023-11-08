package assistant;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomMenu extends JMenuBar {
    private JMenu PatientMenu;
    private JMenu RdvMenu;
    private JMenuItem patientsMenuItem;
    private JMenuItem rendezvousMenuItem;
    JFrame jf;
    public CustomMenu(JFrame jf) {
        this.jf=jf;

        PatientMenu = new JMenu("Patients");
        RdvMenu = new JMenu("RDV");

        patientsMenuItem = new JMenuItem("Liste Patients");
        rendezvousMenuItem = new JMenuItem("Liste Rendez-vous");

        patientsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                openPatientListScreen();
            }
        });

        rendezvousMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                openRdvListScreen();
            }
        });

        PatientMenu.add(patientsMenuItem);
        RdvMenu.add(rendezvousMenuItem);

        add(PatientMenu);
        add(RdvMenu);
    }

    private void openPatientListScreen() {
        jf.setVisible(false);
        new PatientListScreen().setVisible(true);
    }

    private void openRdvListScreen() {
        jf.setVisible(false);
        new RdvListScreen().setVisible(true);
    }
}