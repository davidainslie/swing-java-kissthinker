package com.kissthinker.swing.action;

import java.awt.event.ActionEvent;
import javax.swing.*;
import com.kissthinker.swing.SwingExecutor;
import static com.kissthinker.swing.SwingUtil.execute;

/**
 * @author David Ainslie
 *
 */
public class SwingExecutorAction extends AbstractAction
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    private final SwingExecutor<?, ?> swingExecutor;

    /**
     *
     * @param swingExecutor
     */
    public SwingExecutorAction(SwingExecutor<?, ?> swingExecutor)
    {
        super();
        this.swingExecutor = swingExecutor;
    }

    /**
     *
     * @param name
     * @param icon
     * @param swingExecutor
     */
    public SwingExecutorAction(String name, Icon icon, SwingExecutor<?, ?> swingExecutor)
    {
        super(name, icon);
        this.swingExecutor = swingExecutor;
    }

    /**
     *
     * @param name
     * @param swingExecutor
     */
    public SwingExecutorAction(String name, SwingExecutor<?, ?> swingExecutor)
    {
        super(name);
        this.swingExecutor = swingExecutor;
    }

    /**
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        execute(swingExecutor);
    }
}