package pl.poznan.put.cs.school.gui;

import javax.swing.table.AbstractTableModel;

/**
 * Created by ns on 18.01.18.
 */
class GradesTableModel extends AbstractTableModel
{
    private int columnCount;
    private Object data[][];

    public GradesTableModel(Object[][] data, int columnCount)
    {
        super();
        this.data = data;
        this.columnCount = columnCount;
    }

    /**
     * Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    @Override
    public int getRowCount()
    {
        return data.length;
    }

    /**
     * Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     */
    @Override
    public int getColumnCount()
    {
        return columnCount;
    }

    /**
     * Returns a default name for the column using spreadsheet conventions:
     * A, B, C, ... Z, AA, AB, etc.  If <code>column</code> cannot be found,
     * returns an empty string.
     *
     * @param column the column being queried
     * @return a string containing the default name of <code>column</code>
     */
    @Override
    public String getColumnName(int column)
    {
        if (column == 0)
        {
            return "Uczeń";
        }
        else
        {
            return String.valueOf(column);
        }
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param rowIndex    the row whose value is to be queried
     * @param columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        if(data[rowIndex][columnIndex] == null)
        {
            return "";
        }
        return data[rowIndex][columnIndex];
    }
}
