package com.housingsimulator.model;

import com.housingsimulator.exceptions.WrongEntityTypeException;
import com.housingsimulator.serialization.AutoSerializable;
import com.housingsimulator.simulation.Entity;

import java.io.Serializable;

public abstract class AbstractEntity implements Entity , AutoSerializable, Serializable {
    private String name;
    private int id;
    private static int currentId = 0;

    public static void increaseCurrentId(int newCurrentId) {
        currentId = Math.max(currentId, newCurrentId);
    }


    /**
     * Gets the name of an AbstractEntity
     * @return the name of the entity
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the id of an AbstractEntity
     * @return the id of the entity
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets the name of an AbstractEntity
     * @param name value to set entity's name as
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Creates a clean AbstractEntity
     */
    public AbstractEntity(){
        this.name = "";
        this.id = AbstractEntity.currentId;
        AbstractEntity.currentId++;
    }

    /**
     * Creates an entity setting its values
     * @param name value to set the entity's name as
     */
    public AbstractEntity(String name) {
        this.name = name;
        this.id = AbstractEntity.currentId;
        AbstractEntity.currentId++;
    }

    /**
     * Creates an AbstractEntity setting its values as the same of a given entity
     * @param e the entity to copy the values from
     */
    public AbstractEntity(AbstractEntity e){
        this.name = e.getName();
        this.id = e.getId();
    }

    /**
     * checks if two Entities are equal
     * @param o object to compare to
     * @return true if the objects are equal false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity entity = (AbstractEntity) o;
        return this.id == entity.getId() && this.name.equals(entity.getName());
    }

    /**
     * serializes an AbstractEntity
     * @return string representing an AbstractEntity
     */
    @Override
    public String toString() {
        return String.format("id: %d; name: '%s'", this.id, this.name);
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    /**
     * creates a copy of the Entity object
     * @return a copy of the Entity object
     */
    @Override
    public abstract AbstractEntity clone();

    /**
     * Copies the content of the given object
     * @param ent the entity to copy
     */
    public void copy(AbstractEntity ent) throws WrongEntityTypeException {
        if(ent.getClass() != this.getClass())
            throw new WrongEntityTypeException("Argument is of the wrong class");
        this.name = ent.getName();
        //We dont copy the id
    }
}
