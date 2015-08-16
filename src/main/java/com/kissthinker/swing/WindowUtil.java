package com.kissthinker.swing;

import java.awt.Frame;
import com.kissthinker.object.Singleton;

/**
 * @author David Ainslie
 *
 */
@Singleton
public final class WindowUtil
{
    /** */
    public static Frame activeFrame()
    {
        Frame invisibleFrame = null;

        for (Frame frame : Frame.getFrames())
        {
            if (frame.isVisible())
            {
                return frame;
            }

            invisibleFrame = frame;
        }

        if (invisibleFrame == null)
        {
            throw new RuntimeException("No application frames available!!!");
        }

        return invisibleFrame;
    }

    /**
     * Utility.
     */
    private WindowUtil()
    {
    }
}