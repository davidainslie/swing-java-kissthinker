package com.kissthinker.swing.table;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * @author David Ainslie
 *
 */
public class FreezableScrollPane extends JScrollPane
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    private final  JTable table;

    /**
     * 
     * @param table
     */
    public FreezableScrollPane(JTable table)
    {
        super();

        this.table = table;
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        getViewport().setView(table);
    }

    /**
     * 
     * @param numberColumns
     * @return
     */
    public FreezableScrollPane freezeLeftColumns(int numberColumns)
    {
        JTable leftTable = new JTable(table().getModel());
        leftTable.setSelectionModel(table().getSelectionModel());
        
        TableColumnModel tableColumnModel = table().getColumnModel();
        TableColumnModel leftTableColumnModel = new DefaultTableColumnModel();
        int leftTableColumnsWidth = 0;

        for (int i = 0; i < numberColumns; i++)
        {
            TableColumn tableColumn = tableColumnModel.getColumn(0);
            tableColumnModel.removeColumn(tableColumn);
            leftTableColumnModel.addColumn(tableColumn);
            leftTableColumnsWidth += tableColumn.getPreferredWidth() + leftTableColumnModel.getColumnMargin();
        }

        leftTable.setColumnModel(leftTableColumnModel);
        leftTable.setPreferredScrollableViewportSize(new Dimension(leftTableColumnsWidth, 0));

        JViewport leftViewport = new JViewport();
        leftViewport.setView(leftTable);
        setRowHeader(leftViewport);
        setCorner(JScrollPane.UPPER_LEFT_CORNER, leftTable.getTableHeader());
        
        return this;
    }
    
    /**
     * TODO
     * @param numberColumns
     * @return
     */
    public FreezableScrollPane freezeRightColumns(int numberColumns)
    {
        if ("1".equals("1"))
        {
            // TODO
            throw new UnsupportedOperationException();
        }
        
        JTable rightTable = new JTable(table().getModel());
        rightTable.setSelectionModel(table().getSelectionModel());
        
        TableColumnModel tableColumnModel = table().getColumnModel();
        TableColumnModel rightTableColumnModel = new DefaultTableColumnModel();
        int rightTableColumnsWidth = 0;

        for (int i = 0; i < numberColumns; i++)
        {
            TableColumn tableColumn = tableColumnModel.getColumn(tableColumnModel.getColumnCount() - 1);
            tableColumnModel.removeColumn(tableColumn);
            rightTableColumnModel.addColumn(tableColumn);
            rightTableColumnsWidth += tableColumn.getPreferredWidth() + rightTableColumnModel.getColumnMargin();
        }

        rightTable.setColumnModel(rightTableColumnModel);
        rightTable.setPreferredScrollableViewportSize(new Dimension(rightTableColumnsWidth, 0));

        JViewport rightViewport = new JViewport();
        rightViewport.setView(rightTable);
        setRowHeader(rightViewport);

        setCorner(JScrollPane.UPPER_RIGHT_CORNER, rightTable.getTableHeader());
        
        return this;
    }

    /**
     * @return the table
     */
    private JTable table()
    {
        return table;
    }
}