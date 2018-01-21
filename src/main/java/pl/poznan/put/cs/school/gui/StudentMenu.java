package pl.poznan.put.cs.school.gui;

import pl.poznan.put.cs.school.logic.SchoolManager;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by ns on 25.12.17.
 */
class StudentMenu extends JFrame
{
    private Connection con;
    private SchoolManager schoolManager;
    private JTabbedPane tabbedPane;
    private JPanel panel;
    private JTable gradesTable;
    private JTable presenceTable;
    private final int personID;

    public StudentMenu(Connection con, SchoolManager schoolManager, int personID)
    {
        super("Student menu");
        this.con = con;
        this.schoolManager = schoolManager;
        this.personID = personID;
        setSize(300, 205);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try
        {
            fillGradesTable();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        setContentPane(panel);
        setVisible(true);
    }

    private void fillGradesTable() throws SQLException
    {
        Object[][] data;
        List<String> subjects = schoolManager.getSubjectsList();
        data = new Object[subjects.size()][];
        int counter = 0;
        for (String subject : subjects)
        {
            List<Integer> grades = schoolManager.getGrades(personID, subject);
            System.out.println(grades);
            System.out.println(subject);
            data[counter] = new Object[2];
            data[counter][0] = subject;
            StringBuilder gradesString = new StringBuilder();
            for (int grade : grades)
            {
                gradesString.append(grade).append(' ');
            }
            data[counter][1] = gradesString;
            counter++;
        }
        TableModel dataModel = new StudentsGradesTableModel(data);
        gradesTable.setModel(dataModel);
    }
}
