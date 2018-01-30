package pl.poznan.put.cs.school.structures;

/**
 * Created by ns on 30.01.18.
 */
public class Grade
{
    private int value;
    private String date;

    public Grade(int value, String date)
    {
        this.value = value;
        this.date = date;
    }

    @Override
    public String toString()
    {
        return String.valueOf(value);
    }

    public String getDate()
    {
        return date;
    }
}
