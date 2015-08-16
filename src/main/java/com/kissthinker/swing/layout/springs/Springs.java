package com.kissthinker.swing.layout.springs;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import com.kissthinker.swing.Visibility;

/**
 * @author David Ainslie
 *
 */
public class Springs extends SpringLayout
{
    /** */
    private static final ConstraintParser LEFT_CONSTRAINT_PARSER = new LeftConstraintParser();

    /** */
    private static final ConstraintParser TOP_CONSTRAINT_PARSER = new TopConstraintParser();

    /** */
    private static final ConstraintParser RIGHT_CONSTRAINT_PARSER = new RightConstraintParser();

    /** */
    private static final ConstraintParser BOTTOM_CONSTRAINT_PARSER = new BottomConstraintParser();

    /** */
    private final Map<String, ComponentConstraints> componentConstraintsMap = new LinkedHashMap<String, ComponentConstraints>();

    /** */
    private final Map<String, List<ComponentConstraints>> groupComponentConstraintsMap = new LinkedHashMap<String, List<ComponentConstraints>>();

    /** */
    private final List<List<String>> linkVisibilityIds = new ArrayList<List<String>>();

    /**
     *
     * @param parent
     */
    public Springs(Container parent)
    {
        super();
        parent.setLayout(this);
    }

    /**
     *
     * @param component
     * @param id
     * @param constraints
     * @return
     */
    public Springs add(Component component, String id, String constraints)
    {
        return add(component, id, null, constraints);
    }

    /**
     *
     * @param component
     * @param id
     * @param groupId
     * @param constraints
     * @return
     */
    public Springs add(Component component, String id, String groupId, String constraints)
    {
        ComponentConstraints componentConstraints = new ComponentConstraints(component, id, groupId, constraints);
        getComponentConstraintsMap().put(id, componentConstraints);

        if (groupId != null)
        {
            List<ComponentConstraints> groupComponentConstraints = groupComponentConstraintsMap.get(groupId);

            if (groupComponentConstraints == null)
            {
                groupComponentConstraints = new ArrayList<ComponentConstraints>();
                groupComponentConstraintsMap.put(groupId, groupComponentConstraints);
            }

            groupComponentConstraints.add(componentConstraints);
        }

        return this;
    }

    /**
     * Defer this linkage as the components may not exist yet, unlike method linkVisibility(Component... components)
     * @param ids
     * @return
     */
    public Springs linkVisibility(String... ids)
    {
        linkVisibilityIds.add(Arrays.asList(ids));
        return this;
    }

    /**
     *
     * @param components
     * @return
     */
    public Springs linkVisibility(Component... components)
    {
        Visibility.link(components);
        return this;
    }

    /**
     *
     * @param component
     * @param inverseComponents
     * @return
     */
    public Springs inverseVisibility(Component component, Component... inverseComponents)
    {
        Visibility.inverse(component, inverseComponents);
        return this;
    }

    /**
     *
     * @param id
     * @return
     */
    public ComponentConstraints getComponentConstraints(String id)
    {
        return getComponentConstraintsMap().get(id);
    }

    /**
     * @see java.awt.LayoutManager#preferredLayoutSize(Container)
     */
    @Override
    public Dimension preferredLayoutSize(Container parent)
    {
        layoutContainer(parent);
        int preferredWidth = 0, preferredHeight = 0;
        Integer minimumParentGapWidth = null, minimumParentGapHeight = null;

        for (ComponentConstraints componentConstraints : getComponentConstraintsMap().values())
        {
            preferredWidth = Math.max(preferredWidth, componentConstraints.getComponent().getX() + componentConstraints.getComponent().getWidth());
            preferredHeight = Math.max(preferredHeight, componentConstraints.getComponent().getY() + componentConstraints.getComponent().getHeight());

            Matcher rightMatcher = Pattern.compile("(parent)(\\.)(right)(-)(\\d+)").matcher(componentConstraints.getConstraints());

            if (rightMatcher.find())
            {
                try
                {
                    int gap = Integer.parseInt(rightMatcher.group(5));

                    if (minimumParentGapWidth == null)
                    {
                        minimumParentGapWidth = gap;
                    }
                    else
                    {
                        minimumParentGapWidth = Math.min(minimumParentGapWidth, gap);
                    }
                }
                catch (NumberFormatException e)
                {
                    // Ignore.
                }
            }

            Matcher topMatcher = Pattern.compile("(parent)(\\.)(top)(\\+)(\\d+)").matcher(componentConstraints.getConstraints());

            if (topMatcher.find())
            {
                try
                {
                    int gap = Integer.parseInt(topMatcher.group(5));

                    if (minimumParentGapHeight == null)
                    {
                        minimumParentGapHeight = gap;
                    }
                    else
                    {
                        minimumParentGapHeight = Math.min(minimumParentGapHeight, gap);
                    }
                }
                catch (NumberFormatException e)
                {
                    // Ignore.
                }
            }
        }

        // Include parent's insets and gaps.
        Insets parentInsets = parent.getInsets();

        if (minimumParentGapWidth == null)
        {
            minimumParentGapWidth = 0;
        }

        if (minimumParentGapHeight == null)
        {
            minimumParentGapHeight = 0;
        }

        return new Dimension(preferredWidth + parentInsets.left + parentInsets.right + minimumParentGapWidth,
                             preferredHeight + parentInsets.top + parentInsets.bottom + minimumParentGapHeight);
    }

    /**
     * @see java.awt.LayoutManager#layoutContainer(Container)
     */
    @Override
    public void layoutContainer(Container parent)
    {
        System.out.println("layoutContainer");

        linkVisibilityIds();
        resetComponentConstraints();
        parseComponentConstraints(parent);
        setGroupConstraints(parent);
    }

    /**
     *
     * @return
     */
    protected Map<String, ComponentConstraints> getComponentConstraintsMap()
    {
        return componentConstraintsMap;
    }

    /**
     *
     * @return
     */
    protected Map<String, List<ComponentConstraints>> getGroupComponentConstraintsMap()
    {
        return groupComponentConstraintsMap;
    }

    /**
     *
     * @param component
     * @param parent
     * @return
     */
    private boolean isChild(Component component, Container parent)
    {
        for (Component childComponent : parent.getComponents())
        {
            if (childComponent.equals(component))
            {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param componentConstraints
     * @param parent
     */
    private void parseConstraints(ComponentConstraints componentConstraints, Container parent)
    {
        LEFT_CONSTRAINT_PARSER.parse(componentConstraints, parent);
        TOP_CONSTRAINT_PARSER.parse(componentConstraints, parent);
        RIGHT_CONSTRAINT_PARSER.parse(componentConstraints, parent);
        BOTTOM_CONSTRAINT_PARSER.parse(componentConstraints, parent);
    }

    /**
     *
     * @param componentConstraints
     * @return
     */
    private boolean isSimple(ComponentConstraints componentConstraints)
    {
        return JLabel.class.isAssignableFrom(componentConstraints.getComponent().getClass()) ||
               JTextField.class.isAssignableFrom(componentConstraints.getComponent().getClass()) ||
               AbstractButton.class.isAssignableFrom(componentConstraints.getComponent().getClass()) ||
               JComboBox.class.isAssignableFrom(componentConstraints.getComponent().getClass()) ||
               JSpinner.class.isAssignableFrom(componentConstraints.getComponent().getClass());
    }

    /** */
    private void linkVisibilityIds()
    {
        for (List<String> ids : linkVisibilityIds)
        {
            List<Component> components = new ArrayList<Component>();

            for (String id : ids)
            {
                components.add(getComponentConstraintsMap().get(id).getComponent());
            }

            linkVisibility(components.toArray(new Component[0]));
        }

        linkVisibilityIds.clear();
    }

    /** */
    private void resetComponentConstraints()
    {
        for (ComponentConstraints componentConstraints : getComponentConstraintsMap().values())
        {
            removeLayoutComponent(componentConstraints.getComponent());
        }
    }

    /**
     *
     * @param parent
     */
    private void parseComponentConstraints(Container parent)
    {
        for (ComponentConstraints componentConstraints : getComponentConstraintsMap().values())
        {
            if (!isChild(componentConstraints.getComponent(), parent))
            {
                parent.add(componentConstraints.getComponent());
            }

            parseConstraints(componentConstraints, parent);
        }

        super.layoutContainer(parent);
    }

    /**
     *
     * @param parent
     */
    private void setGroupConstraints(Container parent)
    {
        for (List<ComponentConstraints> groupComponentConstraints : getGroupComponentConstraintsMap().values())
        {
            if (isSimple(groupComponentConstraints.get(0)))
            {
                // Size components in group to maximum size.
                int groupMaximumWidth = 0;
                int groupMaximumHeight = 0;

                for (ComponentConstraints componentConstraints : groupComponentConstraints)
                {
                    groupMaximumWidth = Math.max(groupMaximumWidth, componentConstraints.getComponent().getWidth());
                    groupMaximumHeight = Math.max(groupMaximumHeight, componentConstraints.getComponent().getHeight());
                }

                for (ComponentConstraints componentConstraints : groupComponentConstraints)
                {
                    // Set new size contraint for component in group.
                    getConstraints(componentConstraints.getComponent()).setWidth(Spring.constant(groupMaximumWidth));
                    getConstraints(componentConstraints.getComponent()).setHeight(Spring.constant(groupMaximumHeight));
                }
            }
            else
            {
                // Size components in group to average size.
                int groupWidths = 0;
                int groupHeights = 0;

                for (ComponentConstraints componentConstraints : groupComponentConstraints)
                {
                    groupWidths += componentConstraints.getComponent().getWidth();
                    groupHeights += componentConstraints.getComponent().getHeight();
                }

                for (ComponentConstraints componentConstraints : groupComponentConstraints)
                {
                    // Set new size contraint for component in group.
                    getConstraints(componentConstraints.getComponent()).setWidth(Spring.constant(groupWidths / groupComponentConstraints.size()));
                    getConstraints(componentConstraints.getComponent()).setHeight(Spring.constant(groupHeights / groupComponentConstraints.size()));
                }
            }

            // TODO Handle sizing only by width and only by height
        }

        super.layoutContainer(parent);
    }
}