package pl.poznan.put.cs.school.gui;

import pl.poznan.put.cs.school.logic.SchoolManager;
import pl.poznan.put.cs.school.structures.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
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
    private JLabel nameLabel;
    private JTable presenceTable;
    private final int personID;
    private final int MAX_SIZE = 10;

    public StudentMenu(Connection con, SchoolManager schoolManager, int personID)
    {
        super("Menu ucznia");
        JFrame parent = this;
        Student student = null;
        try
        {
            student = schoolManager.getStudentsData(personID);
        }
        catch (SQLException e)
        {
            System.err.println("Failed to get students data");
        }
        nameLabel.setText(student.toString());
        this.con = con;
        this.schoolManager = schoolManager;
        this.personID = personID;
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try
        {
            fillGradesTable();
            fillPresenceTable();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        setContentPane(panel);
        setVisible(true);
        presenceTable.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);
                int col = presenceTable.getSelectedColumn();
                int row = presenceTable.getSelectedRow();
                Presence presence = null;
                try
                {
                    presence = (Presence) presenceTable.getValueAt(row, col);
                }
                catch (ClassCastException ignore)
                {
                    return;
                }
                JOptionPane.showMessageDialog(parent, presence.getDate(), "Data", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        gradesTable.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);
                super.mouseClicked(e);
                int col = gradesTable.getSelectedColumn();
                int row = gradesTable.getSelectedRow();
                Grade grade = null;
                try
                {
                    grade = (Grade) gradesTable.getValueAt(row, col);
                }
                catch (ClassCastException ignore)
                {
                    return;
                }
                JOptionPane.showMessageDialog(parent, grade.getDate(), "Data", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
    private void fillGradesTable() throws SQLException
    {
        Object[][] data;
        List<String> subjects = schoolManager.getSubjectsList();
        data = new Object[subjects.size()][];
        int counter = 0;
        for (String subject : subjects)
        {
            List<Grade> grades = schoolManager.getGrades_(personID, subject);
            data[counter] = new Object[MAX_SIZE];
            data[counter][0] = subject;
            int gradeCounter = 1;
            for(Grade grade : grades)
            {
               data[counter][gradeCounter] = grade;
               gradeCounter++;
            }
            counter++;
        }
        TableModel dataModel = new StudentsGradesTableModel(data, MAX_SIZE);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        gradesTable.setDefaultRenderer(Object.class, centerRenderer);
        gradesTable.setModel(dataModel);
    }

    public int getPersonID()
    {
        return personID;
    }

    private void fillPresenceTable() throws SQLException
    {
        List<String> subjects = schoolManager.getSubjectsListForStudent(getPersonID());
        Object[][] data = new Object[subjects.size()][];
        int classId = schoolManager.getClassId(getPersonID());
        int counter = 0;
        for(String subject : subjects)
        {
            data[counter] = new Object[MAX_SIZE];
            List<Lesson> lessons = schoolManager.getLessonsList(subject, classId);
            int presenceCounter = 1;
            data[counter][0] = subject;
            for(Lesson lesson : lessons)
            {
               Boolean isPresent = schoolManager.getPresenceForStudent(lesson.getId(), getPersonID());
               Presence presence = new Presence(lesson, isPresent);
               data[counter][presenceCounter] = presence;
               presenceCounter++;
            }
            counter++;
        }
        TableModel tableModel = new StudentsPresenceTableModel(MAX_SIZE, data);
        presenceTable.setModel(tableModel);
    }
}
