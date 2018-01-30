package pl.poznan.put.cs.school.gui;

import pl.poznan.put.cs.school.structures.*;
import pl.poznan.put.cs.school.logic.SchoolManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by ns on 25.12.17.
 */
class TeacherMenu extends JFrame
{
    private int teacherId;
    private Connection connection;
    private SchoolManager schoolManager;
    private JPanel panel;
    private JTabbedPane tabbedPane;
    private JTabbedPane enterPane;
    private JList insertGradesClassList;
    private JList insertGradesSubjectList;
    private JList insertGradesStudentList;
    private JButton insertGradeButton;
    private JFormattedTextField gradeTextField;
    private JList insertPresenceClassList;
    private JList insertPresenceLessonList;
    private JList studentListPresence;
    private JButton enterPresenceButton;
    private JTabbedPane viewPane;
    private JPanel viewGradesPanel;
    private JPanel viewPresencesPanel;
    private JList viewGradesClassList;
    private JList viewSubjectsList;
    private JTable viewGradesTable;
    private JButton viewGradesRefreshButton;
    private JLabel teacherLabel;
    private JList subjectListPresence;
    private JTextPane zaznaczObecnychPrzytrzymującPrzyciskTextPane;
    private JList viewPresenceClassList;
    private JList viewPresenceSubjectList;
    private JTable viewPresenceTable;
    private JButton refreshTeachersPresenceTable;
    private final int MAX_SIZE = 10;

    TeacherMenu(Connection connection, SchoolManager schoolManager, int personID)
    {
        super("Menu nauczyciela");
        Student teacher = null;
        try
        {
            teacher = schoolManager.getStudentsData(personID);
        }
        catch (SQLException e)
        {
            System.err.println("Failed to get teacher data: ");
            System.err.println(e.getLocalizedMessage());
        }
        teacherLabel.setText(teacher.toString());
        TeacherMenu parent = this;
        this.teacherId = personID;
        this.connection = connection;
        this.schoolManager = schoolManager;
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);
        try
        {
            fillClassListsAll(viewGradesClassList);
            fillClassListsAll(insertGradesClassList);
            fillClassListsAll(insertPresenceClassList);
            fillClassListsAll(viewPresenceClassList);
        }
        catch (SQLException e)
        {
            System.err.println("Failed to fill classes lists: ");
            System.err.println(e.getLocalizedMessage());
        }
        setVisible(true);
        viewGradesClassList.addListSelectionListener(e ->
        {
            ClassData classData = (ClassData) viewGradesClassList.getSelectedValue();
            List<String> subjects;
            try
            {
                subjects = schoolManager.getSubjectsListForClass(classData.getId());
            }
            catch (SQLException e1)
            {
                System.err.println("Failed to get subjects list for classID");
                e1.printStackTrace();
                return;
            }
            fillSubjectsLists(viewSubjectsList, subjects);
        });
        viewSubjectsList.addListSelectionListener(e ->
        {
            String name = (String) viewSubjectsList.getSelectedValue();
            ClassData classData = (ClassData) viewGradesClassList.getSelectedValue();
            try
            {
                fillStudentsGradesTable(classData.getId(), name);
            }
            catch (SQLException e1)
            {
                System.err.println("Failed to fill students grades table");
                e1.printStackTrace();
            }
        });
        insertGradesClassList.addListSelectionListener(e ->
        {
            ClassData classData = (ClassData) insertGradesClassList.getSelectedValue();
            try
            {
                fillInsertGradeSubjectList(classData.getId());
            }
            catch (SQLException e1)
            {
                System.err.println("Failed to fill insertGradeSubjectList");
                e1.printStackTrace();
            }
        });
        insertGradesSubjectList.addListSelectionListener(e ->
        {
            // lista uczniow danej klasy
            ClassData classData = (ClassData) insertGradesClassList.getSelectedValue();
            try
            {
                fillStudentsList(insertGradesStudentList, classData.getId());
            }
            catch (SQLException e1)
            {
                System.err.println("Failed to fill students list");
                e1.printStackTrace();
            }
        });
        insertGradeButton.addActionListener(e ->
        {
            if (insertGradesClassList.isSelectionEmpty() || insertGradesSubjectList.isSelectionEmpty()
                    || insertGradesStudentList.isSelectionEmpty())
            {
                JOptionPane.showMessageDialog(parent, "Nie podano koniecznych danych");
            }
            Integer grade = null;
            try
            {
                String tmp = gradeTextField.getText();
                grade = Integer.parseInt(tmp);
            }
            catch (NumberFormatException ignored)
            {
            }
            if (grade == null || grade <= 0 || grade > 6)
            {
                JOptionPane.showMessageDialog(parent, "Podaj ocenę w skali 1-6");
                gradeTextField.setValue("");
                return;
            }
            Student student = (Student) insertGradesStudentList.getSelectedValue();
            String subject = (String) insertGradesSubjectList.getSelectedValue();
            try
            {
                schoolManager.insertGrade(student.getID(), subject, grade);
            }
            catch (SQLException e1)
            {
                JOptionPane.showMessageDialog(parent, "Nie udało się wprowadzić oceny");
                e1.printStackTrace();
            }
            JOptionPane.showMessageDialog(parent, "Wprowadzono ocenę\n" + "Wartość: " + grade.toString() +
                    "\nPrzedmiot: " + subject + "\nUczeń: " + student.toString());
        });
        viewGradesRefreshButton.addActionListener(e ->
        {
            ClassData classData = (ClassData) viewGradesClassList.getSelectedValue();
            String subject = (String) viewSubjectsList.getSelectedValue();
            if (subject == null || classData == null)
            {
                return;
            }
            try
            {
                fillStudentsGradesTable(classData.getId(), subject);
            }
            catch (SQLException e1)
            {
                System.err.println("Failed to fill students grades table");
            }
        });
        insertPresenceClassList.addListSelectionListener(e ->
        {
            ClassData classData = (ClassData) insertPresenceClassList.getSelectedValue();
            List<String> subjects = null;
            try
            {
                subjects = schoolManager.getSubjectsTaughtInClass(classData.getId(), getTeacherId());
            }
            catch (SQLException e1)
            {
                System.err.println("Failed to fill subjects list");
            }
            fillSubjectsLists(subjectListPresence, subjects);
        });
        subjectListPresence.addListSelectionListener(e ->
        {
            studentListPresence.setModel(new DefaultListModel());
            String subject = (String) subjectListPresence.getSelectedValue();
            ClassData classData = (ClassData) insertPresenceClassList.getSelectedValue();
            try
            {
                fillLessonsList(insertPresenceLessonList, schoolManager.getLessonsList(subject, classData.getId()));
            }
            catch (SQLException e1)
            {
                System.err.println("Failed to fill lesson list");
                System.err.println(e1.getLocalizedMessage());
            }
        });
        insertPresenceLessonList.addListSelectionListener(e ->
        {
            ClassData classData = (ClassData) insertPresenceClassList.getSelectedValue();
            try
            {
                fillStudentsList(studentListPresence, classData.getId());
            }
            catch (SQLException e1)
            {
                System.err.println("Failed to fill students list: ");
                System.err.println(e1.getLocalizedMessage());
            }
        });
        enterPresenceButton.addActionListener(e ->
        {
            ClassData classData = (ClassData) insertPresenceClassList.getSelectedValue();
            List<Student> absentStudents = null;
            try
            {
                absentStudents = (List<Student>) schoolManager.getStudentsForClass(classData.getId());
            }
            catch (SQLException e1)
            {
                System.err.println("Failed to get students list for class: ");
                System.err.println(e1.getLocalizedMessage());
                return;
            }
            List<Student> presentStudents = (List<Student>) studentListPresence.getSelectedValuesList();
            absentStudents.removeAll(presentStudents);
            Lesson lesson = (Lesson) insertPresenceLessonList.getSelectedValue();
            schoolManager.enterPresence(lesson.getId(), presentStudents, absentStudents);
        });
        viewPresenceClassList.addListSelectionListener(e ->
        {
            ClassData classData = (ClassData) viewPresenceClassList.getSelectedValue();
            List<String> subjects = null;
            try
            {
                subjects = schoolManager.getSubjectsListForClass(classData.getId());
            }
            catch (SQLException e1)
            {
                System.err.println("Failed to get subjects list for class");
                System.err.println(e1.getLocalizedMessage());
            }
            fillSubjectsLists(viewPresenceSubjectList, subjects);
        });
        viewPresenceSubjectList.addListSelectionListener(e ->
        {
            String subject = (String) viewPresenceSubjectList.getSelectedValue();
            ClassData classData = (ClassData) viewPresenceClassList.getSelectedValue();
            try
            {
                fillTeachersPresenceTable(classData.getId(), subject);
            }
            catch (SQLException e1)
            {
                System.err.println("Failed to fill teachers presence table");
                System.err.println(e1.getLocalizedMessage());
            }
        });
        viewPresenceTable.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);
                int col = viewPresenceTable.getSelectedColumn();
                if (col == 0)
                    return;
                int row = viewPresenceTable.getSelectedRow();
                Presence presence = (Presence) viewPresenceTable.getValueAt(row, col);
                JOptionPane.showMessageDialog(parent, presence.getDate(), "Data", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        refreshTeachersPresenceTable.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ClassData classData = (ClassData) viewPresenceClassList.getSelectedValue();
                String subject = (String) viewPresenceSubjectList.getSelectedValue();
                if (subject == null || classData == null)
                {
                    return;
                }
                try
                {
                    fillTeachersPresenceTable(classData.getId(), subject);
                }
                catch (SQLException e1)
                {
                    System.err.println("Failed to fill teachers presence table");
                }
            }
        });
        viewGradesTable.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);
                int col = viewGradesTable.getSelectedColumn();
                int row = viewGradesTable.getSelectedRow();
                Grade grade = null;
                try
                {
                    grade = (Grade) viewGradesTable.getValueAt(row, col);
                    JOptionPane.showMessageDialog(parent, grade.getDate(), "Data", JOptionPane.INFORMATION_MESSAGE);
                }
                catch(ClassCastException ignore)
                {
                }
            }
        });
    }

    private int getTeacherId()

    {
        return teacherId;
    }

    private void fillLessonsList(JList list, List<Lesson> lessons)
    {
        list.setListData(listToArray(lessons, Lesson.class));
    }

    private void fillInsertGradeSubjectList(int classId) throws SQLException
    {
        List<String> subjects = schoolManager.getSubjectsTaughtInClass(classId, getTeacherId());
        fillSubjectsLists(insertGradesSubjectList, subjects);
    }

    private void fillSubjectsLists(JList list, List<String> subjects)
    {
        list.setListData(listToArray(subjects, String.class));
    }

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

    private void fillTeachersPresenceTable(int classId, String subject) throws SQLException
    {
        List<Student> students = schoolManager.getStudentsForClass(classId);
        Object[][] data = new Object[students.size()][];
        List<Lesson> lessons = schoolManager.getLessonsList(subject, classId);
        int counter = 0;
        for (Student student : students)
        {
            data[counter] = new Object[lessons.size() + 1];
            data[counter][0] = student;
            int presence_counter = 1;
            for (Lesson lesson : lessons)
            {
                Boolean isPresent = schoolManager.getPresenceForStudent(lesson.getId(), student.getID());
                Presence presence = new Presence(lesson, isPresent);
                data[counter][presence_counter] = presence;
                presence_counter++;
            }
            counter++;
        }
        TableModel model = new TeachersPresenceTableModel(data);
        viewPresenceTable.setModel(model);
    }

    private void fillStudentsGradesTable(int classId, String subject) throws SQLException
    {
        Object data[][];
        List<Student> students = schoolManager.getStudentsForClass(classId);
        data = new Object[students.size()][];
        int counter = 0;
        for (Student student : students)
        {
            int id = student.getID();
            List<Grade> grades = schoolManager.getGrades_(id, subject);
            data[counter] = new Object[MAX_SIZE];
            data[counter][0] = student;
            int gradesCounter = 1;
            for(Grade grade : grades)
            {
               data[counter][gradesCounter] = grade;
               gradesCounter++;
            }
            counter++;
        }
        TableModel model = new GradesTableModel(data, MAX_SIZE);
        viewGradesTable.setModel(model);
    }

    private <T> T[] listToArray(List<T> list, Class<T> clazz)
    {
        @SuppressWarnings("unchecked")
        T[] typeArray = (T[]) Array.newInstance(clazz, list.size());
        typeArray = list.toArray(typeArray);
        return typeArray;
    }
}
