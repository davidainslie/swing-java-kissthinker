package com.kissthinker.swing;

import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;
import javax.swing.plaf.basic.BasicLabelUI;

/**
 * @author David Ainslie
 *
 */
class VerticalLabelUI extends BasicLabelUI
{
    static
    {
        labelUI = new VerticalLabelUI(false);
    }

    /** */
    private static final int sLABEL_WIDTH = 24;

    /** */
    private static Rectangle paintIconR = new Rectangle();

    /** */
    private static Rectangle paintTextR = new Rectangle();

    /** */
    private static Rectangle paintViewR = new Rectangle();

    /** */
    private static Insets paintViewInsets = new Insets(0, 0, 0, 0);

    /** */
    protected boolean clockwise;

    /**
     *
     * @param clockwise
     */
    VerticalLabelUI(boolean clockwise)
    {
        super();
        this.clockwise = clockwise;
    }

    /**
     *
     * @param c
     * @return
     */
    public Dimension getPreferredSize(JComponent c)
    {
        Dimension dim = super.getPreferredSize(c);
        return new Dimension(sLABEL_WIDTH, dim.width);
    }

    /**
     *
     * @param g
     * @param c
     */
    public void paint(Graphics g, JComponent c)
    {
        JLabel label = (JLabel)c;
        String text = label.getText();
        Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();

        if ((icon == null) && (text == null))
        {
            return;
        }

        FontMetrics fm = g.getFontMetrics();
        paintViewInsets = c.getInsets(paintViewInsets);

        if (clockwise)
        {
            paintViewR.x = paintViewInsets.top;
            paintViewR.y = paintViewInsets.right;
        }
        else
        {
            paintViewR.x = paintViewInsets.bottom;
            paintViewR.y = paintViewInsets.left;
        }

        // Use inverted height & width
        paintViewR.height = c.getWidth() - (paintViewInsets.left + paintViewInsets.right);
        paintViewR.width = c.getHeight() - (paintViewInsets.top + paintViewInsets.bottom);
        paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
        paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;

        String clippedText = layoutCL(label, fm, text, icon, paintViewR, paintIconR, paintTextR);
        Graphics2D g2 = (Graphics2D)g;
        AffineTransform tr = g2.getTransform();

        if (clockwise)
        {
            g2.rotate(Math.PI / 2);
            g2.translate(0, -c.getWidth());
        }
        else
        {
            g2.rotate(-Math.PI / 2);
            g2.translate(-c.getHeight(), 0);
        }

        if (icon != null)
        {
            icon.paintIcon(c, g, paintIconR.x, paintIconR.y);
        }

        if (text != null)
        {
            int textX = paintTextR.x;
            int textY = paintTextR.y + fm.getAscent();

            if (label.isEnabled())
            {
                paintEnabledText(label, g, clippedText, textX, textY);
            }
            else
            {
                paintDisabledText(label, g, clippedText, textX, textY);
            }
        }

        g2.setTransform(tr);
    }
}