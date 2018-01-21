package pl.poznan.put.cs.school.structures;

/**
 * Created by ns on 19.01.18.
 */
public class ClassData
{
    public final String className;
    private int id;

    public ClassData(int id, int number, String letter)
    {
        this.className = String.valueOf(number) + letter;
        this.id = id;
    }

    @Override
    public String toString()
    {
        return className;
    }

    public int getId()
    {
        return id;
    }
}
