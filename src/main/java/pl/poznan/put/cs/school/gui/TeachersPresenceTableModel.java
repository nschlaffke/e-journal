package pl.poznan.put.cs.school.gui;

import javax.swing.table.AbstractTableModel;

/**
 * Created by ns on 29.01.18.
 */
public class TeachersPresenceTableModel extends AbstractTableModel
{
    private Object[][] data;

    TeachersPresenceTableModel(Object[][] data)
    {
        super();
        this.data = data;
    }

    @Override
    public int getRowCount()
    {
        return data.length;
    }

    @Override
    public String getColumnName(int column)
    {
        if (column == 0)
        {
            return "Uczeń";
        }
        else
        {
            Integer tmp = data[0].length - column;
            return tmp.toString();
        }
    }

    @Override
    public int getColumnCount()
    {
        return data[0].length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return data[rowIndex][columnIndex];
    }
}
