package doctor;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomMenu extends JMenuBar {
    private JMenu PatientMenu;
    private JMenu RdvMenu;
    private JMenu soinMenu;
    private JMenuItem patientsMenuItem;
    private JMenuItem patientsMenuItem2;
    private JMenuItem rendezvousMenuItem;
    private JMenuItem soinMenuItem;

    JFrame jf;
    public CustomMenu(JFrame jf) {
        this.jf=jf;

        PatientMenu = new JMenu("Patients");

        RdvMenu = new JMenu("RDV");
        soinMenu = new JMenu("Soin");

        patientsMenuItem = new JMenuItem("Liste Patients");
        patientsMenuItem2=new JMenuItem("Details") ; 
        rendezvousMenuItem = new JMenuItem("Liste Rendez-vous");
        soinMenuItem = new JMenuItem("Liste soin");


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
        soinMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                openSoinListScreen();
            }
        });
        patientsMenuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                openPatientDetailsScreen();
            }
        });


        PatientMenu.add(patientsMenuItem);
        PatientMenu.add(patientsMenuItem2);
        RdvMenu.add(rendezvousMenuItem);
        soinMenu.add(soinMenuItem);


        add(PatientMenu);
        add(RdvMenu);
        add(soinMenu);

    }

    private void openPatientListScreen() {
        jf.setVisible(false);
        new PatientListScreen().setVisible(true);
    }

    private void openRdvListScreen() {
        jf.setVisible(false);
        new RdvListScreen().setVisible(true);
    }
    private void openSoinListScreen() {
        jf.setVisible(false);
        new SoinListScreen().setVisible(true);
    }
    private void openPatientDetailsScreen() {
        new DetailsScreen().setVisible(true);
    }

}