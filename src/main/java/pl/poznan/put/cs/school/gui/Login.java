package pl.poznan.put.cs.school.gui;

import pl.poznan.put.cs.school.logic.ConnectionManager;
import pl.poznan.put.cs.school.logic.SchoolManager;
import pl.poznan.put.cs.school.structures.UserType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by ns on 18.11.17.
 */
class Login extends JFrame
{
    private JPanel Panel;
    private JButton loginButton;
    private JTextField userNameField;
    private JPasswordField userPasswordField;
    private ConnectionManager connectionManager;
    private static final String SEPARATOR = "_";
    public Login()
    {
        super("Dziennik szkolny");
        JFrame parent = this;
        setSize(300, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(Panel);
        setVisible(true);
        //setResizable(false);
        loginButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String userName = userNameField.getText();
                String password = String.valueOf(userPasswordField.getPassword());
                try
                {
                    connectionManager = new ConnectionManager("localhost", "szkola");
                }
                catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1)
                {
                    JOptionPane.showMessageDialog(parent, "Błąd logowania");
                }
                try
                {
                    Connection con = connectionManager.connect(userName, password);
                    SchoolManager schoolManager = new SchoolManager(con);
                    String[] user = userName.split(SEPARATOR);
                    UserType userType = schoolManager.getUserType(user[0], user[1]);
                    int personID = schoolManager.getPresonId(user[0], user[1]);
                    switch (userType)
                    {
                        case PARENT:
                        case STUDENT:
                            new StudentMenu(con, schoolManager, personID);
                            break;
                        case TEACHER:
                            new TeacherMenu(con, schoolManager, personID);
                    }
                    dispose();
                }
                catch (SQLException ex)
                {
                    JOptionPane.showMessageDialog(parent, "Błąd logowania:\n" + ex.getLocalizedMessage());
                }
            }
        });
    }

}
