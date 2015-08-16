package com.kissthinker.swing;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

/**
 * @author David Ainslie
 *
 */
public class TabbedFrame extends JFrame
{
    /** */
    private static final long serialVersionUID = -5286780610305869321L;

    /** */
    private JTabbedPane tabbedPane;

    /** */
    private Dimension frameDimension;

    /**
     * Class initialisation.
     */
    static
    {
        if (System.getProperty("swing.defaultlaf") == null)
        {
            try
            {
                // Default look and feel.
                UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
            }
            catch (Exception e)
            {
                throw new Error("What the...", e);
            }
        }
    }

    /**
     *
     * @param title
     */
    public TabbedFrame(String title)
    {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createSyntheticComponentEventPublisher();
        createAndDisplay();
    }

    /** */
    @Override
    public TabbedFrame add(final Component component)
    {
        return addTab(component.getClass().getSimpleName(), component);
    }

    /**
     * @param title
     * @param component
     * @return
     */
    public TabbedFrame addTab(final String title, final Component component)
    {
        if (tabbedPane == null)
        {
            tabbedPane = new JTabbedPane();
            TabbedFrame.super.add(tabbedPane);
        }

        tabbedPane.addTab(title, component);

        return TabbedFrame.this;
    }

    /** */
    private TabbedFrame createAndDisplay()
    {
        if (System.getProperty("swing.frame.dimension") == null)
        {
            frameDimension = new Dimension(800, 600);
        }

        setSize(frameDimension);
        setVisible(true);

        return TabbedFrame.this;
    }

    /** */
    private TabbedFrame createSyntheticComponentEventPublisher()
    {
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowActivated(WindowEvent windowEvent)
            {
                for (ComponentListener componentListener : getComponentListeners())
                {
                    componentListener.componentShown(new ComponentEvent(TabbedFrame.this, ComponentEvent.COMPONENT_SHOWN));
                }
            }

            @Override
            public void windowIconified(WindowEvent windowEvent)
            {
                for (ComponentListener componentListener : getComponentListeners())
                {
                    componentListener.componentHidden(new ComponentEvent(TabbedFrame.this, ComponentEvent.COMPONENT_HIDDEN));
                }
            }
        });

        return this;
    }
}