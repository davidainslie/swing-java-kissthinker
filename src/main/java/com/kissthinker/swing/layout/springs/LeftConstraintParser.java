package com.kissthinker.swing.layout.springs;

import javax.swing.SpringLayout;

/**
 * @author David Ainslie
 *
 */
public class LeftConstraintParser extends AbstractConstraintParser
{    
    /** */
    public LeftConstraintParser()
    {
        super();
    }

    /** */
    @Override
    protected String getConstraint(ComponentConstraints componentConstraints)
    {
        return componentConstraints.getLeft();
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
        return "left";
    }

    /** */
    @Override
    protected String getOppositeConstraintType()
    {
        return "right";
    }

    /** */
    @Override
    protected String getSpringConstraintType()
    {
        return SpringLayout.WEST;
    }

    /** */
    @Override
    protected String getOppositeSpringConstraintType()
    {
        return SpringLayout.EAST;
    }
}