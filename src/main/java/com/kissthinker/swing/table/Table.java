package com.kissthinker.swing.table;

import java.util.Vector;
import javax.swing.*;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * @author David Ainslie
 * 
 */
public class Table extends JTable
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    public Table()
    {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param dm
     */
    public Table(TableModel dm)
    {
        super(dm);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param dm
     * @param cm
     */
    public Table(TableModel dm, TableColumnModel cm)
    {
        super(dm, cm);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param numRows
     * @param numColumns
     */
    public Table(int numRows, int numColumns)
    {
        super(numRows, numColumns);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param rowData
     * @param columnNames
     */
    public Table(Vector rowData, Vector columnNames)
    {
        super(rowData, columnNames);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param rowData
     * @param columnNames
     */
    public Table(Object[][] rowData, Object[] columnNames)
    {
        super(rowData, columnNames);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param dm
     * @param cm
     * @param sm
     */
    public Table(TableModel dm, TableColumnModel cm, ListSelectionModel sm)
    {
        super(dm, cm, sm);
        // TODO Auto-generated constructor stub
    }
}