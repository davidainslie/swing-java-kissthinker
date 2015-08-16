package com.kissthinker.swing.layout.springs;

import java.awt.Container;

/**
 * 
 * @author David Ainslie
 *
 */
public interface ConstraintParser
{
    /**
     * 
     * @param componentConstraints
     * @param parent
     */
    void parse(ComponentConstraints componentConstraints, Container parent);
}