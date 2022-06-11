package com.housingsimulator.annotations;

/**
 * Annotation which describes a controller endpoint, i.e, a controller method that can be called by the user
 * by inputting a string which follows the given regex pattern
 */
public @interface Endpoint {
    /**
     *
     * @return the Regex of a command to run the annotated method
     */
    String regex();
}
