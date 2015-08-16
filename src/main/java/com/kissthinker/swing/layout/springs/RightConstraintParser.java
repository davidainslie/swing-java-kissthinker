package com.kissthinker.swing.layout.springs;

import java.awt.*;
import javax.swing.*;

/**
 * @author David Ainslie
 *
 */
public class RightConstraintParser extends AbstractConstraintParser
{
    /** */
    public RightConstraintParser()
    {
        super();
    }

    /** */
    @Override
    protected String getConstraint(ComponentConstraints componentConstraints)
    {
        return componentConstraints.getRight();
    }

    /** */
    @Override
    protected String getConstraintCategory()
    {
        return "left|right";
    }

    /** */
    @Override
    protected String getConstraintType()
    {
        return "right";
    }

    /** */
    @Override
    protected String getOppositeConstraintType()
    {
        return "left";
    }

    /** */
    @Override
    protected String getSpringConstraintType()
    {
        return SpringLayout.EAST;
    }

    /** */
    @Override
    protected String getOppositeSpringConstraintType()
    {
        return SpringLayout.WEST;
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
                springs.putConstraint(SpringLayout.EAST, componentConstraints.getComponent(),
                                      0,
                                      SpringLayout.WEST, componentConstraints.getComponent());
            }
        }
    }
}