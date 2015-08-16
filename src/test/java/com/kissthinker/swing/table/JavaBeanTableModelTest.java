package com.kissthinker.swing.table;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import org.junit.Before;
import org.junit.Test;
import com.kissthinker.collection.map.MapUtil;

/**
 * @author David Ainslie
 * 
 */
public class JavaBeanTableModelTest
{
    /** To test */
    private JavaBeanTableModel<Integer, Bean> javaBeanTableModel;
    
    /** */
    private Map<Integer, Bean> model;

    /**  */
    public JavaBeanTableModelTest()
    {
        super();
    }

    /** */
    @Before
    public void initialise()
    {
        model = MapUtil.<Integer, Bean>hashMap().keyValue(1, new Bean())
                                                .keyValue(2, new Bean().id("99").age(99));
        
        javaBeanTableModel = new JavaBeanTableModel<>(model, Bean.class);        
    }

    /**
     * @throws InterruptedException 
     * 
     */
    @Test
    public void tableModelEventOccurredBecauseOfAJavaBeanPropertyUpdate() throws InterruptedException
    {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        
        new JTable(javaBeanTableModel)
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void tableChanged(TableModelEvent tableModelEvent)
            {
                if (1 == tableModelEvent.getColumn() && 1 == tableModelEvent.getLastRow())
                {
                    countDownLatch.countDown();
                }
            }
        };
        
        model.get(2).age(98);
        countDownLatch.await();
    }
}