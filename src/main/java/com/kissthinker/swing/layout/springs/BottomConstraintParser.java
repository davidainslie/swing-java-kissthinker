package com.kissthinker.swing.layout.springs;

import java.awt.*;
import javax.swing.*;

/**
 * @author David Ainslie
 *
 */
public class BottomConstraintParser extends AbstractConstraintParser
{
    /** */
    public BottomConstraintParser()
    {
        super();
    }

    /** */
    @Override
    protected String getConstraint(ComponentConstraints componentConstraints)
    {
        return componentConstraints.getBottom();
    }

    /** */
    @Override
    protected String getConstraintCategory()
    {
        return "top|bottom";
    }

    /** */
    @Override
    protected String getConstraintType()
    {
        return "bottom";
    }

    /** */
    @Override
    protected String getOppositeConstraintType()
    {
        return "top";
    }

    /** */
    @Override
    protected String getSpringConstraintType()
    {
        return SpringLayout.SOUTH;
    }

    /** */
    @Override
    protected String getOppositeSpringConstraintType()
    {
        return SpringLayout.NORTH;
    }

    /**
     *
     * @param componentConstraints
     * @param parent
     */
    @Override
    public void parse(ComponentConstraints componentConstraints, Container parent)
    {
        super.parse(componentConstraints, parent);
        
        if (parent.getLayout() instanceof Springs)
        {
            Springs springs = (Springs)parent.getLayout();
            
            if (!componentConstraints.getComponent().isVisible())
            {
                springs.putConstraint(SpringLayout.SOUTH, componentConstraints.getComponent(),
                                      0,
                                      SpringLayout.NORTH, componentConstraints.getComponent());
            }
        }
    }
}