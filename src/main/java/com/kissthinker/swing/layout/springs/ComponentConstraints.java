package com.kissthinker.swing.layout.springs;

import java.awt.*;
import java.util.StringTokenizer;

/**
 * @author David Ainslie
 *
 */
public class ComponentConstraints
{        
    /** */
    private final Component component;
   
    /** */
    private final String id;
   
    /** */
    private final String groupId;
   
    /** */
    private final String constraints;

    /**
     * @param component
     * @param id
     * @param constraints
     */
    public ComponentConstraints(Component component, String id, String groupId, String constraints)
    {
        this.component = component;
        this.id = id;
        this.groupId = groupId;
        this.constraints = constraints.replaceAll("\\s*", "");
    }

    /** */
    public Component getComponent()
    {
        return component;
    }

    /** */
    public String getId()
    {
        return id;
    }

    /** */
    public String getGroupId()
    {
        return groupId;
    }

    /** */
    public String getConstraints()
    {
        return constraints;
    }

    /** */
    public String getLeft()
    {
        StringTokenizer stringTokenizer = new StringTokenizer(getConstraints(), ",");

        while (stringTokenizer.hasMoreTokens())
        {
            String constraint = stringTokenizer.nextToken();

            if (constraint.startsWith("left:") || constraint.startsWith("x1:"))
            {
                return constraint.replaceAll("x1", "left");
            }                
        }
        
        return null;
    }

    /** */
    public String getTop()
    {
        StringTokenizer stringTokenizer = new StringTokenizer(getConstraints(), ",");

        while (stringTokenizer.hasMoreTokens())
        {
            String constraint = stringTokenizer.nextToken();

            if (constraint.startsWith("top:") || constraint.startsWith("y1:"))
            {
                return constraint.replaceAll("y1", "top");
            }                
        }
        
        return null;
    }

    /** */
    public String getRight()
    {
        StringTokenizer stringTokenizer = new StringTokenizer(getConstraints(), ",");

        while (stringTokenizer.hasMoreTokens())
        {
            String constraint = stringTokenizer.nextToken();

            if (constraint.startsWith("right:") || constraint.startsWith("x2:"))
            {
                return constraint.replaceAll("x2", "right");
            }                
        }
        
        return null;
    }

    /** */
    public String getBottom()
    {
        StringTokenizer stringTokenizer = new StringTokenizer(getConstraints(), ",");

        while (stringTokenizer.hasMoreTokens())
        {
            String constraint = stringTokenizer.nextToken();

            if (constraint.startsWith("bottom:") || constraint.startsWith("y2:"))
            {
                return constraint.replaceAll("y2", "bottom");
            }                
        }
        
        return null;
    }
}