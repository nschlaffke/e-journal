package pl.poznan.put.cs.school.gui;

import javax.swing.table.AbstractTableModel;

/**
 * Created by ns on 30.01.18.
 */
public class StudentsPresenceTableModel extends AbstractTableModel
{
    private Object data[][];
    private int columnCount;
    public StudentsPresenceTableModel(int columnCount, Object data[][])
    {
        super();
        this.data = data;
        this.columnCount = columnCount;
    }

    @Override
    public String getColumnName(int column)
    {
        if (column == 0)
        {
            return "Przedmiot";
        }
        else
        {
            Integer c = column;
            return c.toString();
        }
    }

    @Override
    public int getRowCount()
    {
        return data.length;
    }

    @Override
    public int getColumnCount()
    {
        return columnCount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        if (data[rowIndex][columnIndex] == null)
        {
            return "";
        }
        return data[rowIndex][columnIndex];
    }
}
