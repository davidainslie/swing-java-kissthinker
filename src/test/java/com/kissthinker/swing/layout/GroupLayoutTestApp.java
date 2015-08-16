package com.kissthinker.swing.layout;

import java.awt.*;
import javax.swing.*;

/**
 * @author David Ainslie
 *
 */
public class GroupLayoutTestApp
{
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.add(new WrapperGroupPanel(new GroupPanel()), BorderLayout.NORTH);
            frame.add(new JButton("West End Girls"), BorderLayout.WEST);
            frame.add(new JButton("Centre"), BorderLayout.CENTER);
            frame.setSize(500, 500);
            frame.setVisible(true);
        });
    }
}

class WrapperGroupPanel extends JPanel
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    WrapperGroupPanel(Component component)
    {
        super();

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(component));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(component));
    }
}

/**
 *
 * @author David Ainslie
 *
 */
class GroupPanel extends JPanel
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    GroupPanel()
    {
        super();

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        JButton hello = new JButton("Hello");

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(hello));

        layout.setVerticalGroup(layout.createParallelGroup()
                .addComponent(hello));
    }
}