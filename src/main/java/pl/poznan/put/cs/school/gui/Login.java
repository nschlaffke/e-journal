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
    private JTextArea statusTextArea;
    private JTextField userNameField;
    private JPasswordField userPasswordField;
    private ConnectionManager connectionManager;
    private static final String SEPARATOR = "_";
    public Login()
    {
        super("Login");
        setSize(300, 205);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(Panel);
        setVisible(true);
        //setResizable(false);
        statusTextArea.setWrapStyleWord(true);
        statusTextArea.setLineWrap(true);
        loginButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                /*
                String userName = new String(userNameField.getText());
                String password = new String(userPasswordField.getPassword());
                */
                String userName = "joanna_kownacka";
                String password = "joanna_kownacka";
                /*
                String userName = "jan_kowalski";
                String password = "jan_kowalski";
                */
                try
                {
                    connectionManager = new ConnectionManager("localhost", "szkola");
                }
                catch (InstantiationException e1)
                {
                    e1.printStackTrace();
                }
                catch (IllegalAccessException e1)
                {
                    e1.printStackTrace();
                }
                catch (ClassNotFoundException e1)
                {
                    e1.printStackTrace();
                }
                try
                {
                    Connection con = connectionManager.connect(userName, password);
                    statusTextArea.append("Connected");
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
                    statusTextArea.append(ex.getLocalizedMessage() + '\n');
                }
            }
        });
    }

}
