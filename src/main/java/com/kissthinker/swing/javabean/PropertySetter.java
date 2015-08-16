package com.kissthinker.swing.javabean;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.Border;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import static com.kissthinker.collection.list.ListUtil.linkedList;
import static com.kissthinker.collection.set.SetUtil.hashSet;

/**
 * DSL for JavaBean (SwingBean) property setting of JComponent.
 * 
 * @author David Ainslie
 *
 */
public class PropertySetter<P>
{      
    /** TODO Should we use WeakReference e.g weakHashSet? */
    private final Set<JComponent> targets = hashSet();
    
    /** */
    private final LinkedList<PropertySetting<P>> propertySettings = linkedList();

    /** */
    private Property property;
    
    /** */
    private JComponent source;
    
    /** */
    private Property sourceProperty;

    /**
     * Create a bound property for given target(s) to gain auto bean updating.
     * The auto bean updating will occur with provided constraints.
     * These constraints will be of the form (for example):
     * editable()
     *     .when(someBean).<someBeanPropertyName>.is(<requirement 1>)
     *     .and(someOtherBean).<someOtherBeanPropertyName>.is(<requirement 2>);
     * @param targets
     * @return PropertySetter<P>
     */
    public static <P> PropertySetter<P> on(JComponent... targets)
    {
        return new PropertySetter<P>(targets);
    }

    /**
     * Background colour property of this {@link PropertySetter}
     * @return PropertySetter<Color> this instance for a fluent interface
     */
    @SuppressWarnings("unchecked")
    public PropertySetter<Color> background()
    {
        return (PropertySetter<Color>)chooseProperty(Property.background);
    }

    /**
     * Foreground colour property of this {@link PropertySetter}
     * @return PropertySetter<Color> this instance for a fluent interface
     */
    @SuppressWarnings("unchecked")
    public PropertySetter<Color> foreground()
    {
        return (PropertySetter<Color>)chooseProperty(Property.foreground);
    }

    /**
     * Border property of this {@link PropertySetter}
     * @return PropertySetter<Border> this instance for a fluent interface
     */
    @SuppressWarnings("unchecked")
    public PropertySetter<Border> border()
    {
        return (PropertySetter<Border>)chooseProperty(Property.border);
    }

    /**
     * 
     * @return PropertySetter<String> this instance for a fluent interface
     */
    @SuppressWarnings("unchecked")
    public PropertySetter<String> toolTipText()
    {
        return (PropertySetter<String>)chooseProperty(Property.toolTipText);
    }

    /**
     * 
     * @return PropertySetter<Font> this instance for a fluent interface
     */
    @SuppressWarnings("unchecked")
    public PropertySetter<Font> font()
    {
        return (PropertySetter<Font>)chooseProperty(Property.font);
    }

    /**
     * 
     * @return PropertySetter<JPopupMenu> this instance for a fluent interface
     */
    @SuppressWarnings("unchecked")
    public PropertySetter<JPopupMenu> popupMenu()
    {
        return (PropertySetter<JPopupMenu>)chooseProperty(Property.componentPopupMenu);
    }

    /**
     * 
     * @return PropertySetter<P> this instance for a fluent interface
     */
    public PropertySetter<P> enabled()
    {
        return apply(Property.enabled, true, false, true);
    }

    /**
     * 
     * @return PropertySetter<P> this instance for a fluent interface
     */
    public PropertySetter<P> visible()
    {
        return apply(Property.visible, true, false, true);
    }

    /**
     * 
     * @param propertyValue
     * @return PropertySetter<P> this instance for a fluent interface
     */
    public PropertySetter<P> is(P propertyValue)
    {
        if (source == null)
        {
            propertySettings.add(new PropertySetting<P>(this, property, propertyValue));
        }
        else
        {
            propertySettings.getLast().listen(source, sourceProperty, propertyValue);
        }
        
        return this;
    }

    /**
     * Note that this method may operate on different types of {@link #targets} which may have different "default" property settings.
     * By not providing an "otherwise" i.e. not calling this method, all targets can "default" to their appropriate type of object.
     * Calling this method means that all {@link #targets} will assume the same given "otherwise" property value. 
     * @param propertyValue any appropriate value to match the property.
     * @return PropertySetter<P> this instance for a fluent interface
     */
    public PropertySetter<P> otherwise(P propertyValue)
    {
        if (source == null)
        {
            propertySettings.getLast().otherwisePropertyValue(propertyValue);
        }
        
        return this;
    }

    /**
     * 
     * @param source
     * @return PropertySetter<P> this instance for a fluent interface
     */
    public PropertySetter<P> when(JComponent source)
    {
        this.source = source;
        return this;
    }

    /**
     * 
     * @param source
     * @return PropertySetter<P> this instance for a fluent interface
     */
    public PropertySetter<P> and(JComponent source)
    {
        propertySettings.getLast().and();
        this.source = source;
        return this;
    }

    /**
     * 
     * @param source
     * @return PropertySetter<P> this instance for a fluent interface
     */
    public PropertySetter<P> or(JComponent source)
    {
        propertySettings.getLast().or();
        this.source = source;
        return this;
    }

    /**
     * @return the targets
     */
    Set<JComponent> targets()
    {
        return targets;
    }

    /**
     * @return the propertySettings
     */
    LinkedList<PropertySetting<P>> propertySettings()
    {
        return propertySettings;
    }

    /**
     * Only allow instantiation via static {@link #on(JComponent...)} method
     * @param targets
     */
    private PropertySetter(JComponent... targets)
    {
        super();
        Collections.addAll(this.targets, targets);
    }

    /**
     * 
     * @param chosenProperty
     * @return PropertySetter<P> this instance for a fluent interface
     */
    private PropertySetter<P> chooseProperty(Property chosenProperty)
    {
        if (property == null)
        {
            property = chosenProperty;
        }
        else
        {
            sourceProperty = chosenProperty;
        }
        
        return this;
    }

    /**
     * 
     * @param requiredProperty
     * @param propertyValue
     * @param defaultPropertyValue
     * @param criteria
     * @return PropertySetter<P> this instance for a fluent interface
     */
    private <V, C> PropertySetter<P> apply(Property requiredProperty, V propertyValue, V defaultPropertyValue, C criteria)
    {
        if (property == null)
        {
            property = requiredProperty;
            propertySettings.add(new PropertySetting<P>(this, property, propertyValue, defaultPropertyValue));
        }
        else
        {
            propertySettings.getLast().listen(source, requiredProperty, criteria);
        }
        
        return this;
    }

    /**
     * Because of Generics erasure, at the time of writing, AspectJ code has to nastily suppress warnings.
     * 
     * @author David Ainslie
     *
     */
    @Aspect
    static class PropertySetterAspect
    {
        /**
         * 
         * @param propertySetter
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        @After("execution(public PropertySetter *.*(..)) && target(propertySetter)")
        public void propertySetterInvocation(PropertySetter propertySetter)
        {
            LinkedList<PropertySetting<?>> linkedList = propertySetter.propertySettings();

            for (PropertySetting<?> propertySetting : linkedList)
            {
                propertySetting.setProperty();
            }
        }
    }
}