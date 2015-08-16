package com.kissthinker.swing.javabean;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import com.kissthinker.concurrency.ExecutorUtil;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author David Ainslie
 * 
 */
public class PropertySetterTestApp
{
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

            tabbedPane.addTab("Swing Property Settings", createGUI());
        });
    }

    /** */
    private static JPanel createGUI()
    {
        // Components.
        final JButton button1 = new JButton("Button One");
        button1.setEnabled(false);

        final JButton button2 = new JButton("Button Two");

        final JButton button3 = new JButton("Button Three");
        button3.setVisible(false);

        final JTextField textField1 = new JTextField("Text field 1");
        final JTextField textField2 = new JTextField("Text field 2");

        // Layout - Any layout management with do, but here is an example of using GroupLayout.
        JPanel container = new JPanel();
        
        GroupLayout gl = new GroupLayout(container);
        container.setLayout(gl);        
        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);
        
        gl.setHorizontalGroup(gl.createSequentialGroup()
            .addComponent(textField1)
            .addComponent(textField2)
            .addGroup(gl.createParallelGroup()
                .addComponent(button1)
                .addComponent(button2)
                .addComponent(button3)));
        
        gl.setVerticalGroup(gl.createSequentialGroup()
            .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(textField2, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(button1))
            .addComponent(button2)
            .addComponent(button3));

        hookupAndValidate(textField1, textField2, button1, button2, button3);
        andAStandardJavaApproach(textField1, textField2, button1, button2, button3);
        
        return container;
    }

    /** */
    private PropertySetterTestApp()
    {
        super();
    }

    /**
     * By "hooking" up swing component javabean properties we have great flexibilty and yet keep our code clean and simple.
     * @param textField1
     * @param textField2
     * @param button1
     * @param button3
     */
    private static void hookupAndValidate(final JTextField textField1,
                                          final JTextField textField2,
                                          final JButton button1,
                                          final JButton button2,
                                          final JButton button3)
    {
        // PropertySetter.on(textField1, textField2).background().is(Color.RED).otherwise(Color.YELLOW)
        // PropertySetter.on(textField1, textField2).background().is(Color.RED)
        PropertySetter.on(textField1, textField2).enabled()
            .when(button1).enabled()
            .and(button3).visible();
        
        // TODO Notice that a "when" condition is repeated - should be able to declare once and reuse.
        PropertySetter.on(textField1).toolTipText().is("After").otherwise("Before")
            .when(button1).background().is(Color.YELLOW);
        
        PropertySetter.on(textField1).font().is(new Font("Sans Serif", Font.BOLD | Font.ITALIC, 18))
            .when(button1).background().is(Color.YELLOW);
        
        PropertySetter.on(textField1).popupMenu().is(createPopupMenu())
            .when(button1).background().is(Color.YELLOW);
        
        PropertySetter.on(button2).border().is(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED, 3), button2.getBorder()))
                                           .otherwise(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), button2.getBorder()))
            .when(button1).enabled()
            .and(button3).visible();
        
        PropertySetter.on(button2).background().is(Color.CYAN).otherwise(Color.YELLOW)
            .when(button1).background().is(Color.YELLOW);
        
        // TODO PropertySetter.on(textField1).property(Fn.function(from(textField1.setEditable(true)));
        
        final CyclicBarrier barrier = new CyclicBarrier(2, () -> {
            try
            {
                TimeUnit.SECONDS.sleep(3);
                assertThat(textField1.getBackground()).isEqualTo(Color.RED);
                assertThat(textField2.getBackground()).isEqualTo(Color.RED);
                TimeUnit.SECONDS.sleep(3);
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            System.exit(1);
        });
        
        ExecutorUtil.schedule(3, TimeUnit.SECONDS, () -> {
            button1.setEnabled(true);

            try
            {
                barrier.await();
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        
        ExecutorUtil.schedule(6, TimeUnit.SECONDS, () -> {
            button1.setBackground(Color.YELLOW);
            button3.setVisible(true);

            try
            {
                barrier.await();
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    /**
     * 
     * @return JPopupMenu
     */
    private static JPopupMenu createPopupMenu()
    {
        JPopupMenu popupMenu = new JPopupMenu();

        popupMenu.add("Scooby Doo");
        
        JMenu subMenu = new JMenu("Bros");
        subMenu.add("Scoopy");
        subMenu.add("Scooty");
        popupMenu.add(subMenu);
        
        return popupMenu;
    }

    /**
     * TODO REMOVE THIS TEST EXAMPLE
     * @param textField1
     * @param textField2
     * @param button1
     * @param button2
     * @param button3
     */
    private static void andAStandardJavaApproach(final JTextField textField1,
                                                 final JTextField textField2,
                                                 final JButton button1,
                                                 final JButton button2,
                                                 final JButton button3)
    {
        // The following only reacts to "property" and "component" changes,
        // so we would still have to perform initial checks to decide if "textField1" and "textField2" should be enabled.
    
        button1.addPropertyChangeListener("enabled", propertyChangeEvent -> {
            if (Boolean.TRUE.equals(propertyChangeEvent.getNewValue()))
            {
                if (button3.isVisible())
                {
                    textField1.setEnabled(true);
                    textField2.setEnabled(true);
                    return;
                }
            }

            textField1.setEnabled(false);
            textField2.setEnabled(false);
        });
        
        // For the "visible" property, have to use a "component listener" instead of a "property change listener". 
        button3.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentShown(ComponentEvent componentEvent)
            {
                if (button1.isEnabled())
                {
                    textField1.setEnabled(true);
                    textField2.setEnabled(true);
                    return;
                }
                
                textField1.setEnabled(false);
                textField2.setEnabled(false);
            }

            @Override
            public void componentHidden(ComponentEvent componentEvent)
            {
                textField1.setEnabled(false);
                textField2.setEnabled(false);
            }
        });        
    }
}