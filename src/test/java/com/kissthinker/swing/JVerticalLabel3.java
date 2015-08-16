package com.kissthinker.swing;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * @author David Ainslie
 *
 */
public class JVerticalLabel3 extends JLabel
{
    /**
     *
     * @param text
     * @param icon
     * @param horizontalAlignment
     */
    public JVerticalLabel3(String text, Icon icon, int horizontalAlignment)
    {
        super(text, icon, horizontalAlignment);
        this.setUI(new VerticalLabelUI(false));
        this.setVerticalAlignment(JLabel.CENTER);
    }

    /**
     *
     * @param text
     * @param horizontalAlignment
     */
    public JVerticalLabel3(String text, int horizontalAlignment)
    {
        this(text, null, horizontalAlignment);
    }

    /**
     *
     * @param text
     */
    public JVerticalLabel3(String text)
    {
        this(text, null, LEADING);
    }

    /**
     *
     * @param image
     * @param horizontalAlignment
     */
    public JVerticalLabel3(Icon image, int horizontalAlignment)
    {
        this(null, image, horizontalAlignment);
    }

    /**
     *
     * @param image
     */
    public JVerticalLabel3(Icon image)
    {
        this(null, image, CENTER);
    }

    /**
     *
     */
    public JVerticalLabel3()
    {
        this("", null, LEADING);
    }
}