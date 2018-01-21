package pl.poznan.put.cs.school.gui;

import pl.poznan.put.cs.school.structures.Student;
import pl.poznan.put.cs.school.logic.SchoolManager;
import pl.poznan.put.cs.school.structures.ClassData;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ComponentAdapter;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by ns on 25.12.17.
 */
class TeacherMenu extends JFrame
{
    public TeacherMenu(Connection connection, SchoolManager schoolManager, int personID)
    {
        super("Teacher menu");
        this.connection = connection;
        this.schoolManager = schoolManager;
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);
        try
        {
            fillClassListsAll(viewGradesClassList);
        }
        catch (SQLException e)
        {
            System.out.println("Failed to fill class list");
            e.printStackTrace();
        }
        setVisible(true);
        viewGradesClassList.addComponentListener(new ComponentAdapter()
        {
        });
        viewGradesClassList.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                ClassData classData = (ClassData) viewGradesClassList.getSelectedValue();
                List<String> subjects = null;
                try
                {
                    subjects = schoolManager.getSubjectsListForClass(classData.getId());
                }
                catch (SQLException e1)
                {
                    System.out.println("Failed to get subjects list for classID");
                    e1.printStackTrace();
                    return;
                }
                fillSubjectsLists(viewSubjectsList, subjects);
            }
        });
        viewSubjectsList.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                String name = (String) viewSubjectsList.getSelectedValue();

            }
        });
    }
    private void fillSubjectsLists(JList list, List<String> subjects)
    {
        list.setListData(listToArray(subjects, String.class));
    }
    private Connection connection;
    private SchoolManager schoolManager;
    private JPanel panel;
    private JTabbedPane tabbedPane;
    private JTabbedPane enterPane;
    private JList ClassList;
    private JList SubjectList;
    private JButton wprowadźButton;
    private JFormattedTextField formattedTextField1;
    private JList ClassList2;
    private JList SubjectList2;
    private JList StudentList;
    private JButton enterGrade;
    private JFormattedTextField formattedTextField2;
    private JList ClassListPresence;
    private JList LessonListPresence;
    private JList StudentListPresence;
    private JCheckBox obecnyCheckBox;
    private JButton enterPresenceButton;
    private JTabbedPane viewPane;
    private JPanel viewGradesPanel;
    private JPanel viewPresencesPanel;
    private JPanel Wiadomości;
    private JPanel viewSchedulesPanel;
    private JList viewGradesClassList;
    private JList viewSubjectsList;
    private JTable StudentsGradesTable;

    private void fillClassListsAll(JList list) throws SQLException
    {
        List<ClassData> classList = schoolManager.getClassList();
        ClassData[] classDataArray = listToArray(classList, ClassData.class);
        list.setListData(classDataArray);
    }
    private void fillStudentsList(JList list, int classID) throws SQLException
    {
        Student[] students = listToArray(schoolManager.getStudentsForClass(classID), Student.class);
        list.setListData(students);
    }
    private void fillStudentsGradesTable()
    {
    }

    private <T> T[] listToArray(List<T> list, Class<T> clazz)
    {
        @SuppressWarnings("unchecked")
        T[] typeArray = (T[]) Array.newInstance(clazz, list.size());
        typeArray = list.toArray(typeArray);
        return typeArray;
    }

    private void fillStudentsGradesTable(int classID, String subject)
    {
        // TODO
    }
}
