package com.kissthinker.swing;

import java.awt.*;
import javax.swing.*;
import com.kissthinker.swing.Label.Orientation;

/**
 * @author David Ainslie
 *
 */
public class VerticalLabelTestApp
{
    /**
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

            tabbedPane.addTab("0", new Label("Scooby Doo").orientation(Orientation.downUp));
            tabbedPane.addTab("1", new JVerticalLabel("Scooby Doo", false));
            tabbedPane.addTab("2", new JVerticalLabel2("Scooby Doo", 0));
            tabbedPane.addTab("3", new JVerticalLabel3("Scooby Doo"));
        });
    }
}

/**
 *
 * @author David Ainslie
 *
 */
class Label extends JLabel
{
    /** */
    enum Orientation {leftRight, rightLeft, downUp, upDown}

    /** */
    private static final long serialVersionUID = 1L;

    /** */
    private Orientation orientation = Orientation.leftRight;

    /**
     *
     * @param text
     */
    Label(String text)
    {
        super(text);
        setBorder(BorderFactory.createLineBorder(Color.red));
    }

    /**
     * @param orientation the orientation to set
     */
    Label orientation(Orientation orientation)
    {
        this.orientation = orientation;
        return this;
    }

    /**
     * @see javax.swing.JComponent#getMinimumSize()
     */
    @Override
    public Dimension getMinimumSize()
    {
        return orientatedSize(super.getMinimumSize());
    }

    /**
     * @see javax.swing.JComponent#getPreferredSize()
     */
    @Override
    public Dimension getPreferredSize()
    {
        return orientatedSize(super.getPreferredSize());
    }

    /**
     * @see java.awt.Component#getSize()
     */
    @Override
    public Dimension getSize()
    {
        return orientatedSize(super.getSize());
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g.create();

        // Note - Don't know why the extra "+ 1" is required to make it look a tad better.
        if (orientation == Orientation.downUp)
        {
            g2d.translate(getWidth() / 2, getHeight() / 2);
            g2d.rotate(-Math.toRadians(90));

            g2d.drawString(getText(), -getHeight() / 2 + 1, getWidth() / 4);
        }
        else if (orientation == Orientation.upDown)
        {
            g2d.translate(getWidth() / 2, getHeight() / 2);
            g2d.rotate(Math.toRadians(90));

            g2d.drawString(getText(), -getHeight() / 2 + 1, getWidth() / 4);
        }

        String text = getText();
        setText("");
        super.paintComponent(g2d);
        g2d.dispose();
        setText(text);
    }

    /**
     *
     * @param dimension
     * @return
     */
    private Dimension orientatedSize(Dimension dimension)
    {
        switch (orientation)
        {
            case downUp:
            case upDown:
                return new Dimension(dimension.height, dimension.width);
        }

        return dimension;
    }
}