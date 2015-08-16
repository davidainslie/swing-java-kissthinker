package com.kissthinker.swing;

import java.util.List;

/**
 *
 * @author David Ainslie
 *
 * @param <R>
 * @param <I>
 */
public abstract class SwingExecutor<R, I>
{
    /** */
    public SwingExecutor()
    {
        super();
    }

    /** */
    protected boolean preExecuteOnEDT()
    {
        return true;
    }

    /**
     * Contract
     * @return R the result of work which can be null for Void
     */
    protected abstract R executeOffEDT();

    /**
     *
     * @param chunks
     */
    protected void executeFeedbackOnEDT(List<I> chunks)
    {
    }

    /**
     *
     * @param result
     */
    protected void postExecuteOnEDT(R result)
    {
    }

    /**
     *
     * @param exception
     */
    protected void executeExceptionOnEDT(Exception exception)
    {
        throw new RuntimeException(exception);
    }
}