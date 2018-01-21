package pl.poznan.put.cs.school.structures;

/**
 * Created by ns on 07.01.18.
 */
public class Message
{
    private final String content;
    private final String title;
    private final String date;

    public Message(String content, String title, String date)
    {
        this.content = content;
        this.title = title;
        this.date = date;
    }

    public String getContent()
    {
        return content;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDate()
    {
        return date;
    }
}
