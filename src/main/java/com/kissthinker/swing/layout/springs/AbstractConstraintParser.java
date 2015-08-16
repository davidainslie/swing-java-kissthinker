package com.kissthinker.swing.layout.springs;

import java.awt.Container;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author David Ainslie
 *
 */
public abstract class AbstractConstraintParser implements ConstraintParser
{
    /** */
    public AbstractConstraintParser()
    {
        super();
    }

    /**
     *
     * @param componentConstraints
     * @param parent
     */
    public void parse(ComponentConstraints componentConstraints, Container parent)
    {
        if (parent.getLayout() instanceof Springs)
        {
            Springs springs = (Springs)parent.getLayout();
            
            String constraint = getConstraint(componentConstraints);

            if (constraint != null)
            {
                int adjustment = 0;
                
                Matcher constraintMatcher = Pattern.compile(constructConstraintRegex()).matcher(constraint);
    
                if (constraintMatcher.find())
                {
                    if (constraintMatcher.group(6) != null)
                    {
                        if (constraintMatcher.group(6).startsWith("+"))
                        {
                            adjustment += Integer.parseInt(constraintMatcher.group(6).substring(1));
                        }
                        else
                        {
                            adjustment -= Integer.parseInt(constraintMatcher.group(6).substring(1));
                        }
                    }
                }

                if ("parent".equals(constraintMatcher.group(3)))
                {
                    if (getConstraintType().equals(constraintMatcher.group(5)))
                    {
                        springs.putConstraint(getSpringConstraintType(), componentConstraints.getComponent(),
                                              adjustment,
                                              getSpringConstraintType(), parent);
                    }
                    else if (getOppositeConstraintType().equals(constraintMatcher.group(5)))
                    {
                        springs.putConstraint(getSpringConstraintType(), componentConstraints.getComponent(),
                                              adjustment,
                                              getOppositeSpringConstraintType(), parent);
                    }
                    else
                    {
                        throw new IllegalArgumentException(String.format("Given constraint %s for component %s makes no sense.",
                                                                         constraint,
                                                                         componentConstraints.getComponent().getClass().getSimpleName()));
                    }
                }
                else
                {
                    String relativeId = constraintMatcher.group(3);
                    ComponentConstraints relativeComponentConstraints = springs.getComponentConstraints(relativeId);
    
                    if (getConstraintType().equals(constraintMatcher.group(5)))
                    {
                        springs.putConstraint(getSpringConstraintType(), componentConstraints.getComponent(),
                                              adjustment,
                                              getSpringConstraintType(), relativeComponentConstraints.getComponent());
                    }
                    else if (getOppositeConstraintType().equals(constraintMatcher.group(5)))
                    {
                        springs.putConstraint(getSpringConstraintType(), componentConstraints.getComponent(),
                                              adjustment,
                                              getOppositeSpringConstraintType(), relativeComponentConstraints.getComponent());
                    }
                    else
                    {
                        throw new IllegalArgumentException(String.format("Given constraint %s for component %s makes no sense.",
                                                                         constraint,
                                                                         componentConstraints.getComponent().getClass().getSimpleName()));
                    }
                }
            }
        }
    }    

    /**
     * Contract.
     * @return
     */
    protected abstract String getConstraint(ComponentConstraints componentConstraints);

    /**
     * Contract.
     * @return
     */
    protected abstract String getConstraintCategory();

    /**
     * Contract.
     * @return
     */    
    protected abstract String getConstraintType();

    /**
     * Contract.
     * @return
     */    
    protected abstract String getOppositeConstraintType();

    /**
     * Contract.
     * @return
     */    
    protected abstract String getSpringConstraintType();

    /**
     * Contract.
     * @return
     */    
    protected abstract String getOppositeSpringConstraintType();

    /**
     * 
     * @return
     */
    private String constructConstraintRegex()
    {
        return String.format("(.*)(:)([^+-]*)(\\.)(%s)([+-]\\d+)?", getConstraintCategory());
    }
}