package pl.poznan.put.cs.school.structures;

/**
 * Created by ns on 07.01.18.
 */
public class Presence
{
    private final String date;
    private final boolean isPresent;

    public Presence(String date, boolean isPresent)
    {
        this.date = date;
        this.isPresent = isPresent;
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
