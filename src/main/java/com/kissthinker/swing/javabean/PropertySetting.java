package com.kissthinker.swing.javabean;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import javax.swing.*;
import com.kissthinker.reflect.MethodUtil;
import static com.kissthinker.collection.list.ListUtil.arrayList;

/**
 * @author David Ainslie
 *
 */
class PropertySetting<P>
{
    /** */
    private static final String PROPERTY_DEFAULT = "PROPERTY_DEFAULT";
    
    /** */
    private final List<Constraint<?>> constraints = arrayList();
    
    /** */
    private final PropertySetter<P> propertySetter;
    
    /** */
    private final Property property;
    
    /** */
    private final Object propertyValue;
    
    /** */
    private Object otherwisePropertyValue;

    /**
     * 
     * @param propertySetter
     * @param property
     * @param propertyValue
     */
    PropertySetting(PropertySetter<P> propertySetter, Property property, Object propertyValue)
    {
        this(propertySetter, property, propertyValue, null);
    }

    /**
     * 
     * @param propertySetter
     * @param property
     * @param propertyValue
     * @param defaultPropertyValue
     */
    PropertySetting(PropertySetter<P> propertySetter, Property property, Object propertyValue, Object defaultPropertyValue)
    {
        super();
        this.propertySetter = propertySetter;
        this.property = property;
        this.propertyValue = propertyValue;
        
        for (JComponent component : propertySetter.targets())
        {
            otherwisePropertyValue = defaultPropertyValue;
            
            if (otherwisePropertyValue == null)
            {
                otherwisePropertyValue = MethodUtil.invokeGetter(component, property.toString());
            }
            
            component.putClientProperty(PROPERTY_DEFAULT, otherwisePropertyValue);
        }
    }

    /**
     * @return the property
     */
    Property property()
    {
        return property;
    }

    /**
     * 
     * @param propertyValue
     * @return PropertySetting<P>
     */
    PropertySetting<P> otherwisePropertyValue(Object propertyValue)
    {
        this.otherwisePropertyValue = propertyValue;
        return this;
    }

    /**
     *
     * @return PropertySetting<P> this instance for a fluent interface.
     */
    PropertySetting<P> and()
    {
        constraints.add(new And());
        return this;
    }

    /**
     * 
     * @return PropertySetting<P> this instance for a fluent interface.
     */
    PropertySetting<P> or()
    {
        constraints.add(new Or());
        return this;
    }

    /**
     * 
     * @param component
     * @param property
     * @param criteria
     * @return PropertySetting<P> this instance for a fluent interface.
     */
    <C> PropertySetting<P> listen(final JComponent component, final Property property, final C criteria)
    {
        constraints.add(new Constraint<C>(component, property, criteria));
        
        if (Property.visible == property)
        {
            component.addComponentListener(new ComponentAdapter()
            {
                @Override
                public void componentShown(ComponentEvent componentEvent)
                {
                    setProperty();
                }

                @Override
                public void componentHidden(ComponentEvent componentEvent)
                {
                    setProperty();
                }
            });
        }
        // TODO THAT CHECKBOX CHECK I ORIGINALLY DID
        else
        {
            component.addPropertyChangeListener(property.toString(), propertyChangeEvent -> setProperty());
        }
        
        return this;
    }

    /** */
    void setProperty()
    {
        // TODO Fn.forEach(list, function)
        
        // "and" constraint is the default.
        boolean or = false;
        
        for (Constraint<?> constraint : constraints)
        {
            if (constraint instanceof And)
            {
                or = false;
            }
            else if (constraint instanceof Or)
            {
                or = true;
            }
            else if (!constraint.satisfied() && !or)
            {
                setProperty(otherwisePropertyValue);
                return;
            }
            else if (or)
            {
                setProperty(propertyValue);
                return;
            }
        }
        
        setProperty(propertyValue);
    }

    /**
     * 
     * @param propertyValue
     */
    void setProperty(final Object propertyValue)
    {
        for (final JComponent target : propertySetter.targets())
        {
            EventQueue.invokeLater(() -> {
                if (propertyValue == null)
                {
                    MethodUtil.invokeSetter(target, property.toString(), target.getClientProperty(PROPERTY_DEFAULT));
                }
                else
                {
                    MethodUtil.invokeSetter(target, property.toString(), propertyValue);
                }

                target.invalidate();
            });
        }
    }
}