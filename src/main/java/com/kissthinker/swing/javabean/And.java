package com.kissthinker.swing.javabean;

import com.kissthinker.object.Type;

/**
 * @author David Ainslie
 *
 */
class And extends Constraint<Void>
{
    /** */
    And()
    {
        super(null, null, Type.VOID);
    }

    /**
     * @see com.kissthinker.swing.javabean.Constraint#satisfied()
     */
    @Override
    boolean satisfied()
    {
        return true;
    }
}