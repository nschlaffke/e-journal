package pl.poznan.put.cs.school.gui;
import javax.swing.SwingUtilities;
import java.sql.SQLException;

/**
 * Created by ns on 18.11.17.
 */
public class DBApp
{
    private static final String userNameSeparator = "-";

    public static void main(String[] args) throws SQLException, IllegalAccessException, ClassNotFoundException, InstantiationException
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                new Login();
            }
        });
    }
}
