package com.kissthinker.swing;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import com.kissthinker.object.Singleton;

/**
 * Utility to handle "visible" property of component(s).
 * @author David Ainslie
 *
 */
@Singleton
public final class Visibility
{
    /**
     * 
     * @param component
     * @return
     */
    public static <C extends Component> C on(C component)
    {
        if (!component.isVisible())
        {
            component.setVisible(true);
            fireVisiblePropertyChange(component, false, true);            
        }
        
        return component;
    }
    
    /**
     * 
     * @param component
     * @return
     */
    public static <C extends Component> C off(C component)
    {
        if (component.isVisible())
        {
            component.setVisible(false);
            fireVisiblePropertyChange(component, true, false);            
        }
        
        return component;
    }

    /**
     * 
     * @param component
     */
    public static <C extends Component> C negate(C component)
    {
        if (component.isVisible()) {
            component.setVisible(false);
            fireVisiblePropertyChange(component, true, false);            
        }
        else
        {
            component.setVisible(true);            
            fireVisiblePropertyChange(component, false, true);            
        }
        
        return component;
    }
    
    /**
     *
     * @param components
     */
    public static void link(final Component... components)
    {
        PropertyChangeListener visiblePropertyChangeListener = propertyChangeEvent -> {
            for (Component component : components)
            {
                component.setVisible((Boolean)propertyChangeEvent.getNewValue());
            }
        };
                
        for (Component component : components)
        {
            component.addPropertyChangeListener("visible", visiblePropertyChangeListener);
        }
    } 
    
    /**
     *     
     * @param component
     * @param inverseComponents
     */
    public static void inverse(final Component component, final Component... inverseComponents)
    {
        component.addPropertyChangeListener("visible", propertyChangeEvent -> {
            for (Component inverseComponent : inverseComponents)
            {
                inverseComponent.setVisible(!(Boolean)propertyChangeEvent.getNewValue());
            }
        });
        
        PropertyChangeListener inverseComponentsVisiblePropertyChangeListener = propertyChangeEvent -> {
            component.setVisible(!(Boolean)propertyChangeEvent.getNewValue());

            for (Component inverseComponent : inverseComponents)
            {
                inverseComponent.setVisible((Boolean)propertyChangeEvent.getNewValue());
            }
        };
        
        for (Component inverseComponent : inverseComponents)
        {
            inverseComponent.addPropertyChangeListener("visible", inverseComponentsVisiblePropertyChangeListener);
        }
    }
    
    /**
     * 
     * @param source
     * @param oldValue
     * @param newValue
     */
    private static void fireVisiblePropertyChange(Component source, boolean oldValue, boolean newValue)
    {
        try
        {
            Method firePropertyChangeMethod = source.getClass().getMethod("firePropertyChange",
                                                                           String.class,
                                                                           boolean.class,
                                                                           boolean.class);
            
            firePropertyChangeMethod.setAccessible(true);
            firePropertyChangeMethod.invoke(source, "visible", oldValue, newValue);
        } 
        catch (Exception e)
        {
            // TODO Log correctly.
            e.printStackTrace();
        }
    }
    
    /**
     * Utility.
     */
    private Visibility() {
        super();
    }    
}