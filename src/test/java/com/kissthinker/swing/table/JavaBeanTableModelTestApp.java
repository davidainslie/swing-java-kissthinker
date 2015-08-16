package com.kissthinker.swing.table;

import static org.junit.Assert.assertEquals;

import java.awt.EventQueue;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import com.kissthinker.collection.map.MapUtil;

/**
 * @author David Ainslie
 * 
 */
public class JavaBeanTableModelTestApp
{
    /** */
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

    /**
     * Bootstrap.
     * @param args
     */
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 500);
            frame.setVisible(true);

            JTabbedPane tabbedPane = new JTabbedPane();
            frame.add(tabbedPane);

            final TableModel tableModel = new JavaBeanTableModel<>(createBeans(), Bean.class);

            // In this test we do not "start up" thread using ExecutorUtil as it needs time to configure itself.
            EXECUTOR_SERVICE.execute(() -> assertEquals(2, tableModel.getColumnCount()));

            tabbedPane.addTab("Java Bean Table Model Test 1", new JScrollPane(new JTable(tableModel)));
        });
    }

    /**
     * 
     * @return
     */
    private static Map<Integer, Bean> createBeans()
    {
        return MapUtil.<Integer, Bean>hashMap().keyValue(1, new Bean())
                                               .keyValue(2, new Bean().id("99").age(99));
    }

    /**
     * 
     */
    private JavaBeanTableModelTestApp()
    {
        super();
    }
}