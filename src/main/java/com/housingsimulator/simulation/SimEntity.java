package com.housingsimulator.simulation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

/**
 * Used to specify explicitly how a SimState's fields map to simulation indexes. May be used in setter methods. Used by Simulator::toStateAs (deprecated)
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SimEntity {
    int index();
}