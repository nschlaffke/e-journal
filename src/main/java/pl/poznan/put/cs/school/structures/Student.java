package pl.poznan.put.cs.school.structures;

import java.util.List;

/**
 * Created by ns on 19.01.18.
 */
public class Student
{
    public Student(int ID, String name, String surname)
    {
        this.ID = ID;
        this.name = name;
        this.surname = surname;
    }

    public int getID()
    {
        return ID;
    }

    @Override
    public String toString()
    {
        return name + " " + surname;
    }

    private final int ID;
    private final String name;
    private final String surname;
}
