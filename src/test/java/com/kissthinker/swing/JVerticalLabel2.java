package com.kissthinker.swing;

import java.awt.*;
import javax.swing.*;

/**
 * @author David Ainslie
 *
 */
public class JVerticalLabel2 extends JLabel
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    private String txt;

    /** */
    private byte hpos = 0;

    /** */
    private byte vpos = 0;

    /** */
    private float translation = 0;

    /**
     *
     * @param s
     * @param trans
     */
    public JVerticalLabel2(String s, float trans)
    {
        super();
        txt = s;
        translation = trans;
    }

    /**
     *
     * @param g
     */
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(12.0, translation);
        g2d.rotate(4.712389);

        if (txt != null)
        {
            g2d.drawString(txt, this.vpos, this.hpos);
        }
    }

    /**
     *
     * @param rect
     */
    public void setBounds(Rectangle rect)
    {
        this.setBounds(rect);
    }

    /**
     *
     * @param alignement
     */
    public void setHorizontalAlignment(int alignement)
    {
        this.hpos = (byte)alignement;
    }

    /**
     *
     * @param alignement
     */
    public void setVerticalAlignment(int alignement)
    {
        this.vpos = (byte)alignement;
    }

    /**
     *
     * @param dim
     */
    public void setSize(Dimension dim)
    {
        super.setSize(dim);
        setPreferredSize(dim);
        setMinimumSize(dim);
        repaint();
    }

    /**
     *
     * @param width
     * @param height
     */
    public void setSize(int width, int height)
    {
        super.setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        repaint();
    }
}