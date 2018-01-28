package pl.poznan.put.cs.school.gui;

import pl.poznan.put.cs.school.structures.Lesson;
import pl.poznan.put.cs.school.structures.Student;
import pl.poznan.put.cs.school.logic.SchoolManager;
import pl.poznan.put.cs.school.structures.ClassData;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JList ClassList;
    private JList SubjectList;
    private JButton wprowadźButton;
    private JFormattedTextField formattedTextField1;
    private JList insertGradesClassList;
    private JList insertGradesSubjectList;
    private JList insertGradesStudentList;
    private JButton enterGrade;
    private JFormattedTextField gradeTextField;
    private JList classListPresence;
    private JList lessonListPresence;
    private JList studentListPresence;
    private JButton enterPresenceButton;
    private JTabbedPane viewPane;
    private JPanel viewGradesPanel;
    private JPanel viewPresencesPanel;
    private JPanel Wiadomości;
    private JPanel viewSchedulesPanel;
    private JList viewGradesClassList;
    private JList viewSubjectsList;
    private JTable viewGradesTable;
    private JButton viewGradesRefreshButton;
    private JLabel teacherLabel;
    private JTextField ZAZNACZOBECNYCHTextField;
    private JList subjectListPresence;

    public int getTeacherId()
    {
        return teacherId;
    }

    public TeacherMenu(Connection connection, SchoolManager schoolManager, int personID)
    {
        super("Teacher menu");
        Student teacher = null;
        try
        {
            teacher = schoolManager.getStudentsData(personID);
        }
        catch (SQLException e)
        {
            System.err.println("Failed to get teacher data");
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
            fillClassListsAll(classListPresence);
        }
        catch (SQLException e)
        {
            System.out.println("Failed to fill class list");
            e.printStackTrace();
        }
        setVisible(true);
        viewGradesClassList.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                ClassData classData = (ClassData) viewGradesClassList.getSelectedValue();
                List<String> subjects;
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
                ClassData classData = (ClassData) viewGradesClassList.getSelectedValue();
                try
                {
                    fillStudentsGradesTable(classData.getId(), name);
                }
                catch (SQLException e1)
                {
                    System.out.println("Failed to fill students grades table");
                    e1.printStackTrace();
                }
            }
        });
        insertGradesClassList.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                ClassData classData = (ClassData) insertGradesClassList.getSelectedValue();
                try
                {
                    fillInsertGradeSubjectList(classData.getId());
                }
                catch (SQLException e1)
                {
                    System.out.println("Failed to fill insertGradeSubjectList");
                    e1.printStackTrace();
                }
            }
        });
        insertGradesSubjectList.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                // lista uczniow danej klasy
                ClassData classData = (ClassData) insertGradesClassList.getSelectedValue();
                try
                {
                    fillStudentsList(insertGradesStudentList, classData.getId());
                }
                catch (SQLException e1)
                {
                    System.out.println("Failed to fill students list");
                    e1.printStackTrace();
                }
            }
        });
        enterGrade.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
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
                    grade = grade.parseInt(tmp);
                }
                catch (NumberFormatException ex)
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
                        "\nprzedmiotu: " + subject + "\nUczniowi " + student.toString());
            }
        });
        viewGradesRefreshButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
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
                    System.out.println("Failed to fill students grades table");
                }
            }
        });
        classListPresence.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                ClassData classData = (ClassData) classListPresence.getSelectedValue();
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
            }
        });
        subjectListPresence.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                studentListPresence.setModel(new DefaultListModel());
                String subject = (String) subjectListPresence.getSelectedValue();
                ClassData classData = (ClassData) classListPresence.getSelectedValue();
                try
                {
                    fillLessonsList(lessonListPresence, schoolManager.getLessonsList(subject, classData.getId()));
                }
                catch (SQLException e1)
                {
                    System.err.println("Failed to fill lesson list");
                    System.err.println(e1.getLocalizedMessage());
                }
            }
        });
        lessonListPresence.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                ClassData classData = (ClassData) classListPresence.getSelectedValue();
                try
                {
                    fillStudentsList(studentListPresence, classData.getId());
                }
                catch (SQLException e1)
                {
                    System.err.println("Failed to fill students list: ");
                    System.err.println(e1.getLocalizedMessage());
                }
            }
        });
        enterPresenceButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // todo polecenie sqla ktore nadpisze wszystkim obecnosci
            }
        });
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

    private void fillStudentsGradesTable(int classId, String subject) throws SQLException
    {
        Object data[][];
        List<Student> students = schoolManager.getStudentsForClass(classId);
        data = new Object[students.size()][];
        int counter = 0;
        for (Student student : students)
        {
            int id = student.getID();
            data[counter] = new Object[2];
            data[counter][0] = student;
            List<Integer> grades = schoolManager.getGrades(id, subject);
            data[counter][1] = buildStringFromGrades(grades);
            counter++;
        }
        TableModel model = new GradesTableModel(data);
        viewGradesTable.setModel(model);
    }

    private <T> T[] listToArray(List<T> list, Class<T> clazz)
    {
        @SuppressWarnings("unchecked")
        T[] typeArray = (T[]) Array.newInstance(clazz, list.size());
        typeArray = list.toArray(typeArray);
        return typeArray;
    }

    private String buildStringFromGrades(List<Integer> grades)
    {
        StringBuilder gradesString = new StringBuilder();
        for (int grade : grades)
        {
            gradesString.append(grade).append(' ');
        }
        return gradesString.toString();
    }

    private void createUIComponents()
    {
        // TODO: place custom component creation code here
    }
}
