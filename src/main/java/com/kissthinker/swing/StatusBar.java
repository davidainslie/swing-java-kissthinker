package com.kissthinker.swing;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.*;

/**
 * @author David Ainslie
 *
 */
public class StatusBar extends JPanel
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    private final JLabel statusLabel = new JLabel();

    /** */
    public StatusBar()
    {
        super();
        initialise();
    }

    /** */
    private void initialise()
    {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(statusLabel);
        add(Box.createHorizontalGlue());
        add(new InformationLabel(ip()));
        add(Box.createHorizontalStrut(5));
        add(new InformationLabel(jps()));
    }

    /**
     *
     * @return
     */
    private String ip()
    {
        try
        {
            InetAddress address = InetAddress.getLocalHost();
            String ip = address.getHostAddress();

            return ip;
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @return
     */
    private String jps()
    {
        return ManagementFactory.getRuntimeMXBean().getName();
    }

    /**
     *
     * @author David Ainslie
     *
     */
    private static class InformationLabel extends JLabel
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         *
         * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
         */
        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D)g.create();

            g2d.setPaint(Color.BLACK);
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

            g2d.dispose();
        }

        /**
         *
         */
        private InformationLabel()
        {
            super();
            // TODO Auto-generated constructor stub
        }

        /**
         * @param image
         * @param horizontalAlignment
         */
        private InformationLabel(Icon image, int horizontalAlignment)
        {
            super(image, horizontalAlignment);
            // TODO Auto-generated constructor stub
        }

        /**
         * @param image
         */
        private InformationLabel(Icon image)
        {
            super(image);
            // TODO Auto-generated constructor stub
        }

        /**
         * @param text
         * @param icon
         * @param horizontalAlignment
         */
        private InformationLabel(String text, Icon icon, int horizontalAlignment)
        {
            super(text, icon, horizontalAlignment);
            // TODO Auto-generated constructor stub
        }

        /**
         * @param text
         * @param horizontalAlignment
         */
        private InformationLabel(String text, int horizontalAlignment)
        {
            super(text, horizontalAlignment);
            // TODO Auto-generated constructor stub
        }

        /**
         * @param text
         */
        private InformationLabel(String text)
        {
            super(text);
            // TODO Auto-generated constructor stub
        }
    }
}