package com.kissthinker.swing.table;

import java.awt.*;
import javax.swing.*;

/**
 * @author David Ainslie
 * 
 */
public class TableTestApp
{
    /**
     * Bootstrap
     * @param args
     */
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            private String[] columnNames()
            {
                return new String[]{"#", "English", "Japanese", "French", "Roman"};
            }

            private Object[][] rowData()
            {
                return new Object[][]
                {
                    {"1", "one", "ichi - \u4E00", "un", "I"},
                    {"2", "two", "ni - \u4E8C", "deux", "II"},
                    {"3", "three", "san - \u4E09", "trois", "III"},
                    {"4", "four", "shi - \u56DB", "quatre", "IV"},
                    {"5", "five", "go - \u4E94", "cinq", "V"},
                    {"6", "six", "roku - \u516D", "treiza", "VI"},
                    {"7", "seven", "shichi - \u4E03", "sept", "VII"},
                    {"8", "eight", "hachi - \u516B", "huit", "VIII"},
                    {"9", "nine", "kyu - \u4E5D", "neur", "IX"},
                    {"10", "ten", "ju - \u5341", "dix", "X"}
                };
            }

            @Override
            public void run()
            {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(500, 500);
                frame.setVisible(true);

                JTabbedPane tabbedPane = new JTabbedPane();
                frame.add(tabbedPane);

                tabbedPane.addTab("Table With 1 Left Fixed Column", new FreezableScrollPane(new Table(rowData(), columnNames())).freezeLeftColumns(2));
            }
        });
    }

    /** */
    public TableTestApp()
    {
        super();
    }
}