package com.kissthinker.swing.javabean;

import javax.swing.*;
import static com.kissthinker.reflect.MethodUtil.*;

/**
 * @author David Ainslie
 *
 */
class Constraint<C>
{
    /** */
    private final JComponent component;
    
    /** */
    private final Property property;
    
    /** */
    private final C criteria;

    /** */
    Constraint(final JComponent component, final Property property, final C criteria)
    {
        super();
        this.component = component;
        this.property = property;
        this.criteria = criteria;
    }

    /** */
    boolean satisfied()
    {
        return criteria.equals(invokeGetter(component, property.toString()));
    }
}