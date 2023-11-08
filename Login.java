import javax.swing.*;

import assistant.PatientListScreen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame implements ActionListener {
    JLabel l1, l2;
    JTextField t1;
    JPasswordField p1;
    JButton b1, b2;

    Login() {
        l1 = new JLabel("Username");
        l1.setBounds(240, 50, 100, 30);
        add(l1);

        l2 = new JLabel("Password");
        l2.setBounds(240, 120, 100, 30);
        add(l2);

        t1 = new JTextField();
        t1.setBounds(350, 50, 180, 30);
        add(t1);

        p1 = new JPasswordField();
        p1.setBounds(350, 120, 180, 30);
        add(p1);
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("./login.png"));
        Image i2 = i1.getImage().getScaledInstance(240, 350, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0, 0, 240, 350);
        add(l3);

        b1 = new JButton("Login");
        b1.setBackground (new Color(42,127,186) );
        b1.setForeground(new Color (255,255,255));
        b1.setBounds(365, 190, 100, 30);
        b1.addActionListener(this);
        add(b1);
        

        setBackground(new Color(221,221,221));

        setLayout(null);
        setSize( 600, 350);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();      
        int x=(int)((dimension.getWidth() - 450)/2);
        int y=(int)((dimension.getHeight() - 450)/2);
        setLocation(x, y);  
        setVisible(true);
        setTitle("Login");
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == b1) {
            String username = t1.getText();
            String password = p1.getText();
            if(username.equals("doctor") && password.equals("test123") ) 
            {
                new doctor.PatientListScreen().setVisible(true); 
                setVisible(false);
            }
            else if(username.equals("assistant")&& password.equals("test123"))
            {
                new PatientListScreen().setVisible(true); 
                setVisible(false);
            }
            else{

                JOptionPane.showMessageDialog(null, "Invalid Login");
                t1.setText("");
                p1.setText("");

            }   

        }

    }

    public static void main(String[] args) {
        new Login();
    }
}