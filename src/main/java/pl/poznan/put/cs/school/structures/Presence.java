package pl.poznan.put.cs.school.structures;

/**
 * Created by ns on 07.01.18.
 */
public class Presence
{
    private String date;
    private Boolean isPresent = null;

    public Presence(Lesson lesson, Boolean isPresent)
    {
        this(lesson.toString(), isPresent);
    }
    public Presence(String date, Boolean isPresent)
    {
        this.date = date;
        this.isPresent = isPresent;
    }

    @Override
    public String toString()
    {
        if (isPresent == null)
        {
            return "??";
        }
        if (isPresent)
        {
            return "ob";
        }
        else
        {
            return "nb";
        }
    }

    public String getDate()
    {
        return date;
    }

    public boolean getPresence()
    {
        return isPresent;
    }
}
