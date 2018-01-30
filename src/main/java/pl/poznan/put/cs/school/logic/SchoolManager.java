package pl.poznan.put.cs.school.logic;

import pl.poznan.put.cs.school.structures.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static pl.poznan.put.cs.school.structures.UserType.PARENT;
import static pl.poznan.put.cs.school.structures.UserType.STUDENT;
import static pl.poznan.put.cs.school.structures.UserType.TEACHER;

/**
 * Created by ns on 25.12.17.
 */
public final class SchoolManager
{
    private PreparedStatement getSubjectsStmt;
    private PreparedStatement getIdStmt;
    private PreparedStatement insertGradeStmt;
    /*
    private PreparedStatement insertLessonStmt;
    private PreparedStatement insertPresenceStmt;
    private PreparedStatement insertStudentStmt;
    private PreparedStatement insertSubjectStmt;
    */
    private PreparedStatement getGradesStmt;
    private PreparedStatement getUserTypeStmt;
    private PreparedStatement getPresenceStmt;
    /*
    private PreparedStatement insertMessageStmt;
    private PreparedStatement getMessagesStmt;
    private PreparedStatement getSubjectIDsForStudentStmt;
    private PreparedStatement getSubjectNameStmt;
    */
    private PreparedStatement getSubjectsListForStudentStmt;
    private PreparedStatement getClassIDStmt;
    private PreparedStatement getClassListStmt;
    private PreparedStatement getStudentsForClassStmt;
    private PreparedStatement getSubjectsListForClassStmt;
    private PreparedStatement getSubjectsTaughtInClassStmt;
    private PreparedStatement getStudentsDataStmt;
    private PreparedStatement getLessonsListStmt;
    private PreparedStatement enterSinglePresenceStmt;
    private PreparedStatement enterSingleAbsenceStmt;
    private PreparedStatement erasePresenceStmt;
    private PreparedStatement getPresenceForStudentStmt;
    private PreparedStatement getGradesStmt_;
    private Connection con;

    public SchoolManager(Connection con)
    {
        this.con = con;
        try
        {
            getSubjectsStmt = con.prepareStatement("SELECT * FROM przedmioty");
            getIdStmt = con.prepareStatement("SELECT id FROM osoby WHERE imie = ? AND nazwisko = ?");
            insertGradeStmt = con.prepareStatement("INSERT INTO oceny(id_osoby, nazwa_przedmiotu, stopien)" +
                    " VALUES(?,?,?)");
            /*
            insertLessonStmt = con.prepareStatement("INSERT INTO lekcje(data_godzina, id_przedmiotu)"
                    + " VALUES (?,?)");
            insertPresenceStmt = con.prepareStatement("INSERT INTO obecnosci" +
                    "(id_lekcji, czy_obecny, id_ucznia)  VALUES(?,?,?)");
            insertStudentStmt = con.prepareStatement("INSERT INTO " +
                    "osoby(imie, nazwisko, czy_uczen, id_klasy) VALUES(?,?,?,?)");
            insertSubjectStmt = con.prepareStatement("INSERT INTO przedmioty(nazwa) VALUES (?)");
            */
            getGradesStmt = con.prepareStatement("SELECT stopien FROM oceny " +
                    "WHERE nazwa_przedmiotu = ? AND id_osoby = ?");
            getUserTypeStmt = con.prepareStatement("SELECT czy_uczen, czy_rodzic, czy_nauczyciel " +
                    "FROM osoby WHERE imie = ? AND nazwisko = ?");
            getPresenceStmt = con.prepareStatement("SELECT data_godzina, czy_obecny " +
                    "FROM obecnosci o JOIN lekcje l ON o.id_lekcji = l.id " +
                    "WHERE id_przedmiotu = ? AND id_ucznia = ? ORDER BY data_godzina ASC");
            /*insertMessageStmt = con.prepareStatement("INSERT INTO " +
                    "wiadomosci(id_nadawcy, id_odbiorcy, tresc) VALUES(?,?,?)");
            getMessagesStmt = con.prepareStatement("SELECT tresc, tytul, data FROM wiadomosci WHERE id_odbiorcy = ?");
            getSubjectIDsForStudentStmt = con.prepareStatement("SELECT id FROM przedmioty_w_planach WHERE id_klasy = ?");
            */
            getClassIDStmt = con.prepareStatement("SELECT id_klasy FROM osoby WHERE id = ?");
            /*
            getSubjectNameStmt = con.prepareStatement("SELECT nazwa FROM przedmioty_w_planach WHERE id = ?");
            */
            getSubjectsListForStudentStmt = con.prepareStatement("SELECT DISTINCT nazwa FROM przedmioty_w_planach WHERE id_klasy = ?");
            getClassListStmt = con.prepareStatement("SELECT id, numer, litera FROM klasy ORDER BY numer, litera");
            getStudentsForClassStmt = con.prepareStatement("SELECT id, imie, nazwisko FROM osoby WHERE id_klasy = ?");
            getSubjectsListForClassStmt = con.prepareStatement("SELECT DISTINCT nazwa FROM przedmioty_w_planach WHERE id_klasy = ?");
            getSubjectsTaughtInClassStmt = con.prepareStatement("SELECT DISTINCT nazwa FROM przedmioty_w_planach " +
                    "WHERE id_nauczyciela = ? AND id_klasy = ?;");
            getStudentsDataStmt = con.prepareStatement("SELECT imie, nazwisko FROM osoby WHERE id = ?");
            getLessonsListStmt = con.prepareStatement("SELECT id, data_godzina FROM lekcje " +
                    "WHERE id_przedmiotu IN (SELECT id  FROM przedmioty_w_planach WHERE nazwa = ? AND id_klasy = ?) " +
                    "ORDER BY data_godzina DESC");
            erasePresenceStmt = con.prepareStatement("DELETE FROM obecnosci WHERE id_lekcji = ?");
            enterSinglePresenceStmt = con.prepareStatement("INSERT INTO obecnosci VALUES (?,1,?)");
            enterSingleAbsenceStmt = con.prepareStatement("INSERT INTO obecnosci VALUES (?, 0, ?)");
            getPresenceForStudentStmt = con.prepareStatement("SELECT czy_obecny FROM obecnosci WHERE id_lekcji = ? AND id_ucznia = ?");
            getGradesStmt_ = con.prepareStatement("SELECT stopien, data  FROM oceny " +
                    "WHERE nazwa_przedmiotu = ? AND id_osoby = ?");
        }
        catch (SQLException e)
        {
            System.err.println("Failed to prepare statements");
        }
    }

    public Boolean getPresenceForStudent(int lessonId, int studentId) throws SQLException
    {
        getPresenceForStudentStmt.setInt(1, lessonId);
        getPresenceForStudentStmt.setInt(2, studentId);
        ResultSet resultSet = getPresenceForStudentStmt.executeQuery();
        Boolean val = null;
        if (resultSet.next())
        {
            if (resultSet.getInt(1) == 1)
            {
                val = Boolean.TRUE;
            } else
            {
                val = Boolean.FALSE;
            }
        }
        return val;
    }

    private void enterSinglePresence(int lessonId, int studentId) throws SQLException
    {
        enterSinglePresenceStmt.setInt(1, lessonId);
        enterSinglePresenceStmt.setInt(2, studentId);
        enterSinglePresenceStmt.executeUpdate();
    }

    private void enterSingleAbsence(int lessonId, int studentId) throws SQLException
    {
        enterSingleAbsenceStmt.setInt(1, lessonId);
        enterSingleAbsenceStmt.setInt(2, studentId);
        enterSingleAbsenceStmt.executeUpdate();
    }

    private void erasePresence(int lessonId) throws SQLException
    {
        erasePresenceStmt.setInt(1, lessonId);
        erasePresenceStmt.executeUpdate();
    }

    public void enterPresence(int lessonId, List<Student> presentStudents, List<Student> absentStudents)
    {
        try
        {
            con.setAutoCommit(false);
            erasePresence(lessonId);
            for (Student student : presentStudents)
            {
                enterSinglePresence(lessonId, student.getID());
            }
            for (Student student : absentStudents)
            {
                enterSingleAbsence(lessonId, student.getID());
            }
            con.commit();
        }
        catch (SQLException e)
        {
            if (con != null)
            {
                try
                {
                    System.err.println("Transaction is being rolled back");
                    System.err.println(e.getLocalizedMessage());
                    con.rollback();
                }
                catch (SQLException e2)
                {
                    System.err.println("Error occured during rollback");
                    System.err.println(e2.getLocalizedMessage());
                }
            }
        }
        finally
        {
            try
            {
                con.setAutoCommit(true);
            }
            catch (SQLException e)
            {
                System.err.println("Failed to enable auto commit mode");
                System.err.println(e.getLocalizedMessage());
            }
        }
    }

    public List<Lesson> getLessonsList(String subject, int classId) throws SQLException
    {
        getLessonsListStmt.setString(1, subject);
        getLessonsListStmt.setInt(2, classId);
        ResultSet resultSet = getLessonsListStmt.executeQuery();
        List<Lesson> lessons = new ArrayList<>();
        while (resultSet.next())
        {
            int id = resultSet.getInt(1);
            String date = resultSet.getString(2);
            date = date.substring(0, date.lastIndexOf(':'));
            lessons.add(new Lesson(id, date));
        }
        return lessons;
    }

    public Student getStudentsData(int id) throws SQLException
    {
        getStudentsDataStmt.setInt(1, id);
        ResultSet resultSet = getStudentsDataStmt.executeQuery();
        resultSet.next();
        String name = resultSet.getString(1);
        String surname = resultSet.getString(2);
        return new Student(id, name, surname);
    }

    public List<String> getSubjectsTaughtInClass(int classId, int teacherId) throws SQLException
    {
        getSubjectsTaughtInClassStmt.setInt(1, teacherId);
        getSubjectsTaughtInClassStmt.setInt(2, classId);
        ResultSet resultSet = getSubjectsTaughtInClassStmt.executeQuery();
        List<String> subjects = new ArrayList<>();
        while (resultSet.next())
        {
            String subject = resultSet.getString(1);
            subjects.add(subject);
        }
        return subjects;
    }

    public List<String> getSubjectsListForClass(int classID) throws SQLException
    {
        getSubjectsListForClassStmt.setInt(1, classID);
        ResultSet resultSet = getSubjectsListForClassStmt.executeQuery();
        List<String> subjects = new ArrayList<>();
        while (resultSet.next())
        {
            String subject = new String(resultSet.getString(1));
            subjects.add(subject);
        }
        return subjects;
    }

    public List<Student> getStudentsForClass(int classID) throws SQLException
    {
        getStudentsForClassStmt.setInt(1, classID);
        ResultSet resultSet = getStudentsForClassStmt.executeQuery();
        List<Student> students = new ArrayList<>();
        while (resultSet.next())
        {
            Student student = new Student(resultSet.getInt(1),
                    resultSet.getString(2), resultSet.getString(3));
            students.add(student);
        }
        return students;
    }

    public List<ClassData> getClassList() throws SQLException
    {
        ResultSet resultSet = getClassListStmt.executeQuery();
        List<ClassData> classes = new ArrayList<>();
        while (resultSet.next())
        {
            ClassData sample_class = new ClassData(resultSet.getInt(1),
                    resultSet.getInt(2), resultSet.getString(3)
            );
            classes.add(sample_class);
        }
        return classes;
    }

    public void insertGrade(int personID, String subject, int grade) throws SQLException
    {
        insertGradeStmt.setInt(1, personID);
        insertGradeStmt.setString(2, subject);
        insertGradeStmt.setInt(3, grade);
        insertGradeStmt.executeUpdate();
    } // TESTED

    /*
    public void insertPresence(int personID, boolean isPresent, int lessonID) throws SQLException
    {
        insertPresenceStmt.setInt(1, personID);
        insertPresenceStmt.setBoolean(2, isPresent);
        insertPresenceStmt.setInt(3, lessonID);
        insertPresenceStmt.executeUpdate();
    } /// TESTED
    */
    public List<Integer> getGrades(int personID, String subjectName) throws SQLException
    {
        getGradesStmt.setInt(2, personID);
        getGradesStmt.setString(1, subjectName);
        List<Integer> gradeList = new ArrayList<>();
        ResultSet resultSet = getGradesStmt.executeQuery();
        while (resultSet.next())
        {
            gradeList.add(resultSet.getInt(1));
        }
        return gradeList;
    } // TESTED

    public List<Grade> getGrades_(int personId, String subject) throws SQLException
    {
        getGradesStmt_.setInt(2, personId);
        getGradesStmt_.setString(1, subject);
        List<Grade> gradeList = new ArrayList<>();
        ResultSet resultSet = getGradesStmt_.executeQuery();
        while (resultSet.next())
        {
            int val = resultSet.getInt(1);
            String date = resultSet.getString(2);
            date = date.substring(0, date.lastIndexOf(':'));
            gradeList.add(new Grade(val, date));
        }
        return gradeList;
    }

    public List<Presence> getPresence(int personId, int subjectId) throws SQLException
    {
        getPresenceStmt.setInt(1, subjectId);
        getPresenceStmt.setInt(2, personId);
        ResultSet resultSet = getPresenceStmt.executeQuery();
        List<Presence> presenceList = new ArrayList<>();
        while (resultSet.next())
        {
            Presence presence = new Presence(resultSet.getString(1), resultSet.getBoolean(2));
            presenceList.add(presence);
        }
        return presenceList;
    } // TESTED

    /*
    public void insertStudent(String imie, String nazwisko, int id_klasy) throws SQLException
    {
        insertStudentStmt.setString(1, imie);
        insertStudentStmt.setString(2, nazwisko);
        insertStudentStmt.setBoolean(3, true);
        insertStudentStmt.setInt(4, id_klasy);
        insertStudentStmt.executeUpdate();
    } // TESTED

    public void insertMessage(int senderID, int recieverID, String text) throws SQLException
    {
        insertMessageStmt.setInt(1, senderID);
        insertMessageStmt.setInt(2, recieverID);
        insertMessageStmt.setString(3, text);
        insertMessageStmt.executeUpdate();
    } // TESTED

    public List<Message> getMessages(int recieverID) throws SQLException
    {
        getMessagesStmt.setInt(1, recieverID);
        List<Message> messageList = new ArrayList<>();
        ResultSet resultSet = getMessagesStmt.executeQuery();
        while (resultSet.next())
        {
            String content = resultSet.getString(1);
            String title = resultSet.getString(2);
            String date = resultSet.getString(3);
            Message message = new Message(content, title, date);
            messageList.add(message);
        }
        return messageList;
    } // TESTED

    public void insertSubject(String name) throws SQLException
    {
        insertSubjectStmt.setString(1, name);
        insertSubjectStmt.executeUpdate();
    } // TESTED
    */
    public List<String> getSubjectsList() throws SQLException
    {
        ResultSet resultSet = getSubjectsStmt.executeQuery();
        List<String> stringList = new ArrayList<>();
        while (resultSet.next())
        {
            String subject = resultSet.getString(1);
            stringList.add(subject);
        }
        return stringList;
    } // TESTED

    public UserType getUserType(String name, String surname) throws SQLException
    {
        getUserTypeStmt.setString(1, name);
        getUserTypeStmt.setString(2, surname);
        ResultSet rs = getUserTypeStmt.executeQuery();
        rs.next();
        if (rs.getInt(1) == 1)
        {
            return STUDENT;
        } else if (rs.getInt(2) == 1)
        {
            return PARENT;
        } else
        {
            return TEACHER;
        }
    } // TESTED

    public int getPresonId(String name, String surname) throws SQLException
    {
        getIdStmt.setString(1, name);
        getIdStmt.setString(2, surname);
        ResultSet rs = getIdStmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }
    /*
    public List<Integer> getSubjectIDsForStudent(int personID) throws SQLException
    {
        int classID = getClassId(personID);
        getSubjectIDsForStudentStmt.setInt(1, classID);
        List<Integer> subjectList = new ArrayList<>();
        ResultSet resultSet = getSubjectIDsForStudentStmt.executeQuery();
        while (resultSet.next())
        {
            int subjectID = resultSet.getInt(1);
            subjectList.add(subjectID);
        }
        return subjectList;
    }
    */
    public int getClassId(int studentID) throws SQLException
    {
        getClassIDStmt.setInt(1, studentID);
        ResultSet resultSet = getClassIDStmt.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }
    /*
    public void insertLesson(String timeStamp, int subjectId) throws SQLException
    {
        insertLessonStmt.setString(1, timeStamp);
        insertLessonStmt.setInt(2, subjectId);
        insertLessonStmt.executeUpdate();
    }
    */
    /*
    public String getSubjectName(int subjectID) throws SQLException
    {
        getSubjectNameStmt.setInt(1, subjectID);
        ResultSet resultSet = getSubjectNameStmt.executeQuery();
        resultSet.next();
        return resultSet.getString(1);
    }
    */
    public List<String> getSubjectsListForStudent(int personID) throws SQLException
    {
        int classID = getClassId(personID);
        getSubjectsListForStudentStmt.setInt(1, classID);
        ResultSet rs = getSubjectsListForStudentStmt.executeQuery();
        List<String> subjectList = new ArrayList<>();
        while (rs.next())
        {
            String subject = rs.getString(1);
            subjectList.add(subject);
        }
        return subjectList;
    }
}