package com.kissthinker.swing;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * @author David Ainslie
 *
 */
public class AlphaTestApp
{
    /**
     * Bootstrap
     * @param args
     */
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setLayout(new GridLayout(0, 1));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new AlphaPanel("Scooby Doo"));
            frame.pack();
            frame.setVisible(true);
        });
    }
}

/**
 * 
 * @author David Ainslie
 *
 */
class AlphaPanel extends JPanel
                 implements ActionListener
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    private static final Font FONT = new Font("Serif", Font.PLAIN, 32);

    /** */
    private static final float DELTA = -0.05f;

    /** */
    private final Timer timer = new Timer(100, null);

    /** */
    private final String text;

    /** */
    private float alpha = 1f;

    /**
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        alpha += DELTA;

        if (alpha < 0)
        {
            alpha = 1;
            timer.restart();
        }

        repaint();
    }

    /**
     *
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setFont(FONT);
        int x = getWidth();
        int y = getHeight();
        int w2 = g.getFontMetrics().stringWidth(text) / 2;
        int h2 = g.getFontMetrics().getDescent();
        g2d.fillRect(0, 0, x, y);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setPaint(Color.red);
        g2d.drawString(text, x / 2 - w2, y / 2 + h2);
    }

    /**
     *
     * @param text
     */
    AlphaPanel(String text)
    {
        this.text = text;
        setPreferredSize(new Dimension(256, 96));
        setOpaque(true);
        setBackground(Color.black);

        timer.setInitialDelay(1000);
        timer.addActionListener(this);
        timer.start();
    }
}