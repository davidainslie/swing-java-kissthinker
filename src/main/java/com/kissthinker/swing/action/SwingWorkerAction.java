package com.kissthinker.swing.action;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.*;

/**
 * @author David Ainslie
 *
 */
public abstract class SwingWorkerAction<R, I> extends AbstractAction
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    public SwingWorkerAction()
    {
        super();
    }

    /**
     *
     * @param name
     * @param icon
     */
    public SwingWorkerAction(String name, Icon icon)
    {
        super(name, icon);
    }

    /**
     *
     * @param name
     */
    public SwingWorkerAction(String name)
    {
        super(name);
    }

    /**
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        if (preWorkOnEDT())
        {
            new SwingWorker<R, I>()
            {
                @Override
                protected R doInBackground() throws Exception
                {
                    return workOffEDT();
                }

                @Override
                protected void process(List<I> chunks)
                {
                    // TODO Auto-generated method stub
                    // Show feedback about remote/async call - even though we should only know that it's a time-consuming call
                    // and that we have no knowledge of whether is a local or remote call.
                    // Feedback could be in glass pane and the task should be cancelable.
                    super.process(chunks);
                    feedbackOnEDT(chunks);
                }

                @Override
                protected void done()
                {
                    // TODO Auto-generated method stub
                    super.done();

                    try
                    {
                        postWorkOnEDT(get());
                    }
                    catch (Exception e)
                    {
                        exceptionOnEDT(e);
                    }
                }
            };
        }
    }

    /**
     *
     * @return
     */
    protected boolean preWorkOnEDT()
    {
        return true;
    }

    /**
     * Contract
     *
     * @return R the result of work which can be null for Void
     */
    protected abstract R workOffEDT();

    /**
     *
     * @param chunks
     */
    protected void feedbackOnEDT(List<I> chunks)
    {
    }

    /**
     *
     * @param result
     */
    protected void postWorkOnEDT(R result)
    {
    }

    /**
     *
     * @param exception
     */
    protected void exceptionOnEDT(Exception exception)
    {
        throw new RuntimeException(exception);
    }
}