package pl.poznan.put.cs.school.logic;

import java.sql.*;

/**
 * Created by ns on 25.12.17.
 */
public class ConnectionManager
{
    private String url;

    public ConnectionManager(String serverName, String myDatabase) throws
            InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        String driverName = "com.mysql.jdbc.Driver";
        url = "jdbc:mysql://" + serverName + "/" + myDatabase;
        Class.forName(driverName).newInstance();
    }

    public Connection connect(String username, String password) throws SQLException
    {
        Connection con;
        con = DriverManager.getConnection(url, username, password);
        return con;
    }
}
