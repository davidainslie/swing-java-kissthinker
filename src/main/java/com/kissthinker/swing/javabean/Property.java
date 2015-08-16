package com.kissthinker.swing.javabean;

/**
 * Note that the "constants" are not defined in captials (as standard) e.g. instead of TOOL_TIP_TEXT we have {@link #toolTipText}.<br/>
 * This avoids overcomplications when converting to string to be used as property name for a PropertyChangeListener.
 * 
 * @author David Ainslie
 *
 */
public enum Property
{
    /** */
    enabled,
    
    /** */
    visible,
    
    /** */
    background,
    
    /** */
    foreground,
    
    /** */
    border,
    
    /** */
    font,
    
    /** */
    toolTipText,
    
    /** */
    componentPopupMenu
}