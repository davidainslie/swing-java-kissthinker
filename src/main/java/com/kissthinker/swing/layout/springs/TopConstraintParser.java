/**
 * 
 */
package com.kissthinker.swing.layout.springs;


import javax.swing.SpringLayout;


/**
 * @author David Ainslie
 *
 */
public class TopConstraintParser extends AbstractConstraintParser
{
    /**
     * 
     */
    public TopConstraintParser()
    {
        super();
    }


    /**
     * 
     */
    @Override
    protected String getConstraint(ComponentConstraints componentConstraints)
    {
        return componentConstraints.getTop();
    }
    
    
    /**
     * 
     */
    @Override
    protected String getConstraintCategory()
    {
        return "top|bottom";
    }
    
    
    /**
     * 
     */
    @Override
    protected String getConstraintType()
    {
        return "top";
    }
    
    
    /**
     * 
     */
    @Override
    protected String getOppositeConstraintType()
    {
        return "bottom";
    }
    
    
    /**
     * 
     */
    @Override
    protected String getSpringConstraintType()
    {
        return SpringLayout.NORTH;
    }
    
    
    /**
     * 
     */
    @Override
    protected String getOppositeSpringConstraintType()
    {
        return SpringLayout.SOUTH;
    }
}
