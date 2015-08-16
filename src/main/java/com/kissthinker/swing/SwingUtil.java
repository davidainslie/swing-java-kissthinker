package com.kissthinker.swing;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import com.kissthinker.function.Function;
import com.kissthinker.object.Singleton;

/**
 * @author David Ainslie
 *
 */
@Singleton
public final class SwingUtil
{
    /**
     *
     * @param <R>
     * @param <I>
     * @param swingExecutor
     */
    public static <R, I> void execute(final SwingExecutor<R, I> swingExecutor)
    {
        if (swingExecutor.preExecuteOnEDT())
        {
            new SwingWorker<R, I>()
            {
                @Override
                protected R doInBackground() throws Exception
                {
                    return swingExecutor.executeOffEDT();
                }

                @Override
                protected void process(List<I> chunks)
                {
                    // TODO Auto-generated method stub
                    // Show feedback about remote/async call - even though we should only know that it's a time-consuming call
                    // and that we have no knowledge of whether is a local or remote call.
                    // Feedback could be in glass pane and the task should be cancelable.
                    swingExecutor.executeFeedbackOnEDT(chunks);
                }

                @Override
                protected void done()
                {
                    // TODO Auto-generated method stub
                    super.done();

                    try
                    {
                        swingExecutor.postExecuteOnEDT(get());
                    }
                    catch (Exception e)
                    {
                        swingExecutor.executeExceptionOnEDT(e);
                    }
                }
            }.execute();
        }
    }

    /**
     * 
     * @param function
     * @param args
     */
    public static void invokeLater(final Function<?> function, final Object... args)
    {
        EventQueue.invokeLater(() -> function.apply(args));
    }

    /**
     * Utility.
     */
    private SwingUtil()
    {
        super();
    }
}