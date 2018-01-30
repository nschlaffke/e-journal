package pl.poznan.put.cs.school.structures;

/**
 * Created by ns on 27.01.18.
 */
public class Lesson
{
    private int id;
    private String date;
    public Lesson(int id, String date)
    {
        this.id = id;
        this.date = date;
    }

    public int getId()
    {
        return id;
    }

    @Override
    public String toString()
    {
        return date;
    }
}
