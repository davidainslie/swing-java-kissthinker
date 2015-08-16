package com.kissthinker.swing.javabean;

import com.kissthinker.object.Type;

/**
 * @author David Ainslie
 *
 */
class Or extends Constraint<Void>
{
    /** */
    Or()
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